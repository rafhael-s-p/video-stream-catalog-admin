package com.studies.catalog.admin.domain.genre;

import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import helpers.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void givenAnActiveGenre_whenCallInactivate_shouldReceiveOK() {
        final var expectedName = "Terror";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var currentGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(currentGenre);
        Assertions.assertTrue(currentGenre.isActive());
        Assertions.assertNull(currentGenre.getDeletedAt());

        final var currentCreatedAt = currentGenre.getCreatedAt();
        final var currentUpdatedAt = currentGenre.getUpdatedAt();

        currentGenre.inactivate();

        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories().size());
        Assertions.assertEquals(currentCreatedAt, currentGenre.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNotNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() {
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var currentGenre = Genre.newGenre(expectedName, false);

        Assertions.assertNotNull(currentGenre);
        Assertions.assertFalse(currentGenre.isActive());
        Assertions.assertNotNull(currentGenre.getDeletedAt());

        final var currentCreatedAt = currentGenre.getCreatedAt();
        final var currentUpdatedAt = currentGenre.getUpdatedAt();

        currentGenre.activate();

        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories().size());
        Assertions.assertEquals(currentCreatedAt, currentGenre.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() {
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("789"));

        final var currentGenre = Genre.newGenre("Nerror", false);

        Assertions.assertNotNull(currentGenre);
        Assertions.assertFalse(currentGenre.isActive());
        Assertions.assertNotNull(currentGenre.getDeletedAt());

        final var currentCreatedAt = currentGenre.getCreatedAt();
        final var currentUpdatedAt = currentGenre.getUpdatedAt();

        currentGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(currentCreatedAt, currentGenre.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        final var expectedName = "Terror";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("789"));

        final var currentGenre = Genre.newGenre("Nerror", true);

        Assertions.assertNotNull(currentGenre);
        Assertions.assertTrue(currentGenre.isActive());
        Assertions.assertNull(currentGenre.getDeletedAt());

        final var currentCreatedAt = currentGenre.getCreatedAt();
        final var currentUpdatedAt = currentGenre.getUpdatedAt();

        currentGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(currentCreatedAt, currentGenre.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNotNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("789"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var currentGenre = Genre.newGenre("Nerror", false);

        final var currentException = Assertions.assertThrows(NotificationException.class,
                () -> currentGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("789"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var currentGenre = Genre.newGenre("Nerror", false);

        final var currentException = Assertions.assertThrows(NotificationException.class,
                () -> currentGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() {
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var currentGenre = Genre.newGenre("Nerror", expectedIsActive);

        final var currentCreatedAt = currentGenre.getCreatedAt();
        final var currentUpdatedAt = currentGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            currentGenre.update(expectedName, expectedIsActive, null);
        });

        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(currentCreatedAt, currentGenre.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

}