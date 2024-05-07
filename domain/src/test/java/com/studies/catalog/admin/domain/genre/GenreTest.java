package com.studies.catalog.admin.domain.genre;

import com.studies.catalog.admin.domain.exceptions.NotificationException;
import helpers.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenreTest extends TestHelper {

    @Test
    void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var currentGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(currentGenre);
        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories().size());
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var currentException = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var currentException = Assertions.assertThrows(NotificationException.class,
                () -> Genre.newGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = faker.lorem().fixedString(257);
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var currentException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

}