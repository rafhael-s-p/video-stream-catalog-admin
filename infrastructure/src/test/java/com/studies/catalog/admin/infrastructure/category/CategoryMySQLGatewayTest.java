package com.studies.catalog.admin.infrastructure.category;

import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategorySearchQuery;
import com.studies.catalog.admin.infrastructure.MySQLGatewayTest;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(documentaries.getId(), currentResult.items().get(0).getId());
    }

    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(0, currentResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(documentaries.getId(), currentResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;

        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(movies.getId(), currentResult.items().get(0).getId());

        // Page 2
        expectedPage = 2;

        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(series.getId(), currentResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(documentaries.getId(), currentResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategoriesAndMostWatchedAsTerms_whenCallsFindAllAndTermsMatchCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "The most watched category", true);
        final var series = Category.newCategory("Series", "A Category", true);
        final var documentaries = Category.newCategory("Documentaries", "The less watched category", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MOST WATCHED", "name", "asc");
        final var currentResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, currentResult.currentPage());
        Assertions.assertEquals(expectedPerPage, currentResult.perPage());
        Assertions.assertEquals(expectedTotal, currentResult.total());
        Assertions.assertEquals(expectedPerPage, currentResult.items().size());
        Assertions.assertEquals(movies.getId(), currentResult.items().get(0).getId());
    }

}