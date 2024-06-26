package com.studies.catalog.admin.application.category.retrieve.get;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GetCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetCategoryByIdUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        final var currentCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, currentCategory.id());
        Assertions.assertEquals(expectedName, currentCategory.name());
        Assertions.assertEquals(expectedDescription, currentCategory.description());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), currentCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), currentCategory.deletedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 321 was not found";
        final var expectedId = CategoryID.from("321");

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var currentException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("321");

        when(categoryGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var currentException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}
