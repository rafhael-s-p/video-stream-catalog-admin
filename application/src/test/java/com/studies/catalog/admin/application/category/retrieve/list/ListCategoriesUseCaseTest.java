package com.studies.catalog.admin.application.category.retrieve.list;

import com.studies.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.studies.catalog.admin.application.category.retrieve.list.ListCategoriesUseCaseImpl;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCategoriesUseCaseTest {

    @InjectMocks
    private ListCategoriesUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Movies", null, true),
                Category.newCategory("Series", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedResult, currentResult);
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(categories.size(), currentResult.total());
    }

    @Test
    void givenAValidQuery_whenItHasNoResults_thenShouldReturnEmptyCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedResult, currentResult);
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(categories.size(), currentResult.total());
    }

    @Test
    void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var currentException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}
