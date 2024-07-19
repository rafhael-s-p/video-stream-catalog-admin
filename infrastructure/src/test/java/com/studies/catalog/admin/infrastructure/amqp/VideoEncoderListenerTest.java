package com.studies.catalog.admin.infrastructure.amqp;

import com.studies.catalog.admin.AmqpTest;
import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusInput;
import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.studies.catalog.admin.domain.utils.IdUtils;
import com.studies.catalog.admin.domain.video.MediaStatus;
import com.studies.catalog.admin.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.studies.catalog.admin.infrastructure.configuration.json.Json;
import com.studies.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import com.studies.catalog.admin.infrastructure.video.models.VideoEncoderCompleted;
import com.studies.catalog.admin.infrastructure.video.models.VideoEncoderError;
import com.studies.catalog.admin.infrastructure.video.models.VideoMessage;
import com.studies.catalog.admin.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        // given
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "Video not found"
        );

        final var expectedMessage = Json.writeValueAsString(expectedError);

        // when
        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        // then
        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var currentMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, currentMessage);
    }

    @Test
    void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata =
                new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        final var expectedMessage = Json.writeValueAsString(aResult);

        doNothing().when(updateMediaStatusUseCase).execute(any());

        // when
        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        // then
        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var currentMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, currentMessage);

        final var inputCaptor = ArgumentCaptor.forClass(UpdateMediaStatusInput.class);
        verify(updateMediaStatusUseCase).execute(inputCaptor.capture());

        final var currentInput = inputCaptor.getValue();
        Assertions.assertEquals(expectedStatus, currentInput.status());
        Assertions.assertEquals(expectedId, currentInput.videoId());
        Assertions.assertEquals(expectedResourceId, currentInput.resourceId());
        Assertions.assertEquals(expectedEncoderVideoFolder, currentInput.folder());
        Assertions.assertEquals(expectedFilePath, currentInput.filename());
    }

}