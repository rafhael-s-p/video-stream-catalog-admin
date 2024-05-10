package com.studies.catalog.admin.application.category.delete;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCategoryUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Movies", "The most watched category", true);
        final var expectedId = aCategory.getId();

        doNothing()
                .when(categoryGateway)
                .deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("321");

        doNothing()
                .when(categoryGateway)
                .deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategory = Category.newCategory("Movies", "The most watched category", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

}
