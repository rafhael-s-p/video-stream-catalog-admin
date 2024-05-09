package com.studies.catalog.admin.application.category.update;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Moviies", null, true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(1, categoryRepository.count());

        final var currentOutput = useCase.execute(aCommand).get();

        assertNotNull(currentOutput);
        assertNotNull(currentOutput.id());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, currentCategory.getName());
        assertEquals(expectedDescription, currentCategory.getDescription());
        assertEquals(expectedIsActive, currentCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("Moviies", null, true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Moviies", null, true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var currentOutput = useCase.execute(aCommand).get();

        assertNotNull(currentOutput);
        assertNotNull(currentOutput.id());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, currentCategory.getName());
        assertEquals(expectedDescription, currentCategory.getDescription());
        assertEquals(expectedIsActive, currentCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        assertNotNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory = Category.newCategory("Movies", null, true);

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(aCategory.getName(), currentCategory.getName());
        assertEquals(aCategory.getDescription(), currentCategory.getDescription());
        assertEquals(aCategory.isActive(), currentCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), currentCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), currentCategory.getDeletedAt());
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;
        final var expectedId = "321";
        final var expectedErrorMessage = "Category with ID 321 was not found";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var currentException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}