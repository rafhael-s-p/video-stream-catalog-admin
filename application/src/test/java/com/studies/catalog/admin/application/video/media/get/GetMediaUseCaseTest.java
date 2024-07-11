package com.studies.catalog.admin.application.video.media.get;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.video.MediaResourceGateway;
import com.studies.catalog.admin.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetMediaUseCaseImpl useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    void givenVideoIdAndType_whenItIsValidInput_shouldReturnResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var anInput = GetMediaInput.with(expectedId.getValue(), expectedType.name());

        // when
        final var currentResult = this.useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedResource.name(), currentResult.name());
        Assertions.assertEquals(expectedResource.content(), currentResult.content());
        Assertions.assertEquals(expectedResource.contentType(), currentResult.contentType());
    }

    @Test
    void givenVideoIdAndType_whenItIsNotFound_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var anInput = GetMediaInput.with(expectedId.getValue(), expectedType.name());

        // when
        Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(anInput));
    }

    @Test
    void givenVideoIdAndType_whenTypeDoesNotExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media type ANY doesn't exists";

        final var anInput = GetMediaInput.with(expectedId.getValue(), "ANY");

        // when
        final var currentException = Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(anInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}