package com.studies.catalog.admin.application.video.retrieve.get;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.studies.catalog.admin.domain.Fixture.Videos.image;
import static com.studies.catalog.admin.domain.Fixture.Videos.video;
import static com.studies.catalog.admin.domain.video.VideoMediaType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetVideoByIdUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    void givenAValidId_whenCallsGetVideo_shouldReturnIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final var expectedVideo = video(VIDEO);
        final var expectedTrailer = video(VIDEO);
        final var expectedBanner = image(BANNER);
        final var expectedThumb = image(THUMBNAIL);
        final var expectedThumbHalf = image(THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentVideo = this.useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), currentVideo.id());
        Assertions.assertEquals(expectedTitle, currentVideo.title());
        Assertions.assertEquals(expectedDescription, currentVideo.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), currentVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.duration());
        Assertions.assertEquals(expectedOpened, currentVideo.opened());
        Assertions.assertEquals(expectedPublished, currentVideo.published());
        Assertions.assertEquals(expectedRating, currentVideo.rating());
        Assertions.assertEquals(asString(expectedCategories), currentVideo.categories());
        Assertions.assertEquals(asString(expectedGenres), currentVideo.genres());
        Assertions.assertEquals(asString(expectedMembers), currentVideo.castMembers());
        Assertions.assertEquals(expectedVideo, currentVideo.video());
        Assertions.assertEquals(expectedTrailer, currentVideo.trailer());
        Assertions.assertEquals(expectedBanner, currentVideo.banner());
        Assertions.assertEquals(expectedThumb, currentVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, currentVideo.thumbnailHalf());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), currentVideo.updatedAt());
    }

    @Test
    void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Video with ID 123 was not found";

        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var currentError = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, currentError.getMessage());
    }

}