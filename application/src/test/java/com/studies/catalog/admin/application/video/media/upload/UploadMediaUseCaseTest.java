package com.studies.catalog.admin.application.video.media.upload;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.video.MediaResourceGateway;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoMediaType;
import com.studies.catalog.admin.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UploadMediaUseCaseImpl useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway, videoGateway);
    }

    @Test
    void givenInputToUpload_whenItIsValid_shouldUpdateVideoMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.video(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedType, currentOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), currentOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(currentVideo ->
                Objects.equals(expectedMedia, currentVideo.getVideo().get())
                        && currentVideo.getTrailer().isEmpty()
                        && currentVideo.getBanner().isEmpty()
                        && currentVideo.getThumbnail().isEmpty()
                        && currentVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenInputToUpload_whenItIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.video(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedType, currentOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), currentOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(currentVideo ->
                currentVideo.getVideo().isEmpty()
                        && Objects.equals(expectedMedia, currentVideo.getTrailer().get())
                        && currentVideo.getBanner().isEmpty()
                        && currentVideo.getThumbnail().isEmpty()
                        && currentVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenInputToUpload_whenItIsValid_shouldUpdateBannerMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedType, currentOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), currentOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(currentVideo ->
                currentVideo.getVideo().isEmpty()
                        && currentVideo.getTrailer().isEmpty()
                        && Objects.equals(expectedMedia, currentVideo.getBanner().get())
                        && currentVideo.getThumbnail().isEmpty()
                        && currentVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenInputToUpload_whenItIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedType, currentOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), currentOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(currentVideo ->
                currentVideo.getVideo().isEmpty()
                        && currentVideo.getTrailer().isEmpty()
                        && currentVideo.getBanner().isEmpty()
                        && Objects.equals(expectedMedia, currentVideo.getThumbnail().get())
                        && currentVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenInputToUpload_whenItIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertEquals(expectedType, currentOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), currentOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(currentVideo ->
                currentVideo.getVideo().isEmpty()
                        && currentVideo.getTrailer().isEmpty()
                        && currentVideo.getBanner().isEmpty()
                        && currentVideo.getThumbnail().isEmpty()
                        && Objects.equals(expectedMedia, currentVideo.getThumbnailHalf().get())
        ));
    }

    @Test
    void givenInputToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var anInput = UploadMediaInput.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var currentException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(anInput)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}