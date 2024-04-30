package com.studies.catalog.admin.application.category.update;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.application.update.UpdateCategoryCommand;
import com.studies.catalog.admin.application.update.UpdateCategoryUseCase;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

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

        save(aCategory);

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

        Assertions.assertEquals(1, categoryRepository.count());

        final var currentOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("Moviies", null, true);

        save(aCategory);

        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Moviies", null, true);

        save(aCategory);

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

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var currentOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        Assertions.assertNotNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var aCategory = Category.newCategory("Movies", null, true);

        save(aCategory);

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

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var currentCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), currentCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), currentCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), currentCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), currentCategory.getDeletedAt());
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;
        final var expectedId = "321";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 321 was not found";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }

}