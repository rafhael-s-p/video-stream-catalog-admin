package com.studies.catalog.admin.application.video.media.update;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.video.MediaStatus;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UpdateMediaStatusUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    void givenInputForVideo_whenItIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.video(expectedType);

        final var aVideo = Fixture.Videos.theGodfather()
                .setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UpdateMediaStatusInput.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        // when
        this.useCase.execute(anInput);

        // then
        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var currentVideo = captor.getValue();

        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());

        final var currentVideoMedia = currentVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), currentVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), currentVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), currentVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, currentVideoMedia.status());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), currentVideoMedia.encodedLocation());
    }

    @Test
    void givenInputForVideo_whenItIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.video(expectedType);

        final var aVideo = Fixture.Videos.theGodfather()
                .setVideo(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UpdateMediaStatusInput.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        // when
        this.useCase.execute(anInput);

        // then
        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var currentVideo = captor.getValue();

        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());

        final var currentVideoMedia = currentVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), currentVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), currentVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), currentVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, currentVideoMedia.status());
        Assertions.assertTrue(currentVideoMedia.encodedLocation().isBlank());
    }

    @Test
    void givenInputForTrailer_whenItIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.video(expectedType);

        final var aVideo = Fixture.Videos.theGodfather()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UpdateMediaStatusInput.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        // when
        this.useCase.execute(anInput);

        // then
        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var currentVideo = captor.getValue();

        Assertions.assertTrue(currentVideo.getVideo().isEmpty());

        final var currentVideoMedia = currentVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), currentVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), currentVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), currentVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, currentVideoMedia.status());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), currentVideoMedia.encodedLocation());
    }

    @Test
    void givenInputForTrailer_whenItIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.video(expectedType);

        final var aVideo = Fixture.Videos.theGodfather()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UpdateMediaStatusInput.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename
        );

        // when
        this.useCase.execute(anInput);

        // then
        verify(videoGateway, times(1)).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).update(captor.capture());

        final var currentVideo = captor.getValue();

        Assertions.assertTrue(currentVideo.getVideo().isEmpty());

        final var currentVideoMedia = currentVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), currentVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), currentVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), currentVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, currentVideoMedia.status());
        Assertions.assertTrue(currentVideoMedia.encodedLocation().isBlank());
    }

    @Test
    void givenInputForTrailer_whenItIsInvalid_shouldDoNothing() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.video(expectedType);

        final var aVideo = Fixture.Videos.theGodfather()
                .setTrailer(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        final var anInput = UpdateMediaStatusInput.with(
                expectedStatus,
                expectedId.getValue(),
                "randomId",
                expectedFolder,
                expectedFilename
        );

        // when
        this.useCase.execute(anInput);

        // then
        verify(videoGateway, times(0)).update(any());
    }

}