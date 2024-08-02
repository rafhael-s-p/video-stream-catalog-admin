package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.UnitTest;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoValidatorTest extends UnitTest {

    @Test
    void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = null;
        final var expectedDescription = Fixture.description255();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = Fixture.description256();
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4001();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.TV_MA;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

    @Test
    void givenNullRating_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "The Godfather";
        final var expectedDescription = Fixture.description4000();
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDuration = 175.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

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

        // when
        final var currentError = Assertions.assertThrows(
                DomainException.class,
                () -> currentVideo.validate(new ThrowsValidationHandler())
        );

        // then
        Assertions.assertEquals(expectedErrorCount, currentError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentError.getErrors().get(0).message());
    }

}