package com.studies.catalog.admin.domain.category;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.UnitTest;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest extends UnitTest {

    @Test
    void givenValidParams_whenCallNewCategory_thenInstantiateANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(currentCategory);
        Assertions.assertNotNull(currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveOneError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var currentException =
                Assertions.assertThrows(DomainException.class, () -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "  ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var currentException =
                Assertions.assertThrows(DomainException.class, () -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "Mo ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var currentException =
                Assertions.assertThrows(DomainException.class, () -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = Fixture.description258();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var currentException =
                Assertions.assertThrows(DomainException.class, () -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveOK() {
        final var expectedName = "Movies";
        final var expectedDescription = "  ";
        final var expectedIsActive = true;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(currentCategory);
        Assertions.assertNotNull(currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveOK() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var currentCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(currentCategory);
        Assertions.assertNotNull(currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNotNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallInactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var currentCategory = aCategory.inactivate();

        Assertions.assertDoesNotThrow(() -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(createdAt, currentCategory.getCreatedAt());
        Assertions.assertTrue(currentCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var currentCategory = aCategory.activate();

        Assertions.assertDoesNotThrow(() -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(createdAt, currentCategory.getCreatedAt());
        Assertions.assertTrue(currentCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final var nameWithTypo = "Movvies";
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(nameWithTypo, "Some other category", true);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var currentCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> currentCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertEquals(createdAt, currentCategory.getCreatedAt());
        Assertions.assertTrue(currentCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(aCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movies", "Some other category", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var currentCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertEquals(createdAt, currentCategory.getCreatedAt());
        Assertions.assertTrue(currentCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(aCategory.getDeletedAt());
    }

}
