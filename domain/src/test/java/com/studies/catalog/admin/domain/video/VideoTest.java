package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.UnitTest;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.utils.InstantUtils;
import com.studies.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoTest extends UnitTest {

    @Test
    void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        // when
        final var currentVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertNotNull(currentVideo.getCreatedAt());
        Assertions.assertNotNull(currentVideo.getUpdatedAt());
        Assertions.assertEquals(currentVideo.getCreatedAt(), currentVideo.getUpdatedAt());
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());
        Assertions.assertTrue(currentVideo.getDomainEvents().isEmpty());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedEvent = new VideoMediaCreated("ID", "file");
        final var expectedEventCount = 1;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                "Some Title",
                "Some Description",
                Year.of(1994),
                0.0,
                true,
                true,
                Rating.TV_MA,
                Set.of(),
                Set.of(),
                Set.of()
        );

        aVideo.registerEvent(expectedEvent);

        // when
        final var currentVideo = Video.with(aVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        Assertions.assertEquals(expectedEventCount, currentVideo.getDomainEvents().size());
        Assertions.assertEquals(expectedEvent, currentVideo.getDomainEvents().get(0));

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdateVideo_Media_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aVideoMedia =
                VideoMedia.with("abc", "Video.mp4", "/123/videos");

        // when
        final var currentVideo = Video.with(aVideo).updateVideoMedia(aVideoMedia);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertEquals(aVideoMedia, currentVideo.getVideo().get());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdateTrailer_Media_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aTrailerMedia =
                VideoMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var currentVideo = Video.with(aVideo).updateTrailerMedia(aTrailerMedia);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertEquals(aTrailerMedia, currentVideo.getTrailer().get());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdateBanner_Media_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aBannerMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var currentVideo = Video.with(aVideo).updateBannerMedia(aBannerMedia);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertEquals(aBannerMedia, currentVideo.getBanner().get());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdateThumbnail_Media_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var currentVideo = Video.with(aVideo).updateThumbnailMedia(aThumbMedia);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertEquals(aThumbMedia, currentVideo.getThumbnail().get());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdateThumbnailMediaHalf_shouldReturnUpdated() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var currentVideo = Video.with(aVideo).updateThumbnailHalfMedia(aThumbMedia);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), currentVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertEquals(aThumbMedia, currentVideo.getThumbnailHalf().get());

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        // when
        final var currentVideo = Video.with(
                VideoID.unique(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                InstantUtils.now(),
                InstantUtils.now(),
                null,
                null,
                null,
                null,
                null,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        Assertions.assertNotNull(currentVideo.getDomainEvents());
    }

}