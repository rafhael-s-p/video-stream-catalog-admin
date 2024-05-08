package com.studies.catalog.admin.application.category.retrieve.list;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.application.retrieve.list.ListCategoriesUseCase;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@IntegrationTest
class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                        Category.newCategory("Movies", null, true),
                        Category.newCategory("Netflix Originals", "Netflix Originals Categories", true),
                        Category.newCategory("Amazon Originals", "Categories from Amazon Originals", true),
                        Category.newCategory("Documentaries", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Children", "Category for your daughters and sons", true),
                        Category.newCategory("Series", null, true)
                )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    void givenAValidTerm_whenTermDoesNotMatchPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "some random term";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "mov,0,10,1,1,Movies",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "Ch,0,10,1,1,Children",
            "daughters,0,10,1,1,Children",
            "from Amazon,0,10,1,1,Amazon Originals",
    })
    void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedCategoryName, currentResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Movies",
            "createdAt,desc,0,10,7,7,Series",
    })
    void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedCategoryName, currentResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Children",
            "1,2,2,7,Documentaries;Movies",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var currentResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, currentResult.items().size());
        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String currentName = currentResult.items().get(index).name();
            Assertions.assertEquals(expectedName, currentName);
            index++;
        }
    }

}
