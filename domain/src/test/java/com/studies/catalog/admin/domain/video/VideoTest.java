package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import helpers.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoTest extends TestHelper {

    @Test
    void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = faker.lorem().fixedString(4000);
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

        Assertions.assertDoesNotThrow(() -> currentVideo.validate(new ThrowsValidationHandler()));
    }

}