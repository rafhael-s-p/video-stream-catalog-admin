package com.studies.catalog.admin.infrastructure.category;

import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryID;
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

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var currentCategory = categoryGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), currentCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), currentCategory.getDeletedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var currentCategory = categoryGateway.findById(CategoryID.from("emptyId"));

        Assertions.assertTrue(currentCategory.isEmpty());
    }

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var currentCategory = categoryGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), currentCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), currentCategory.getDeletedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());

        final var currentEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), currentEntity.getId());
        Assertions.assertEquals(expectedName, currentEntity.getName());
        Assertions.assertEquals(expectedDescription, currentEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, currentEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), currentEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), currentEntity.getDeletedAt());
        Assertions.assertNull(currentEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movis", null, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var currentInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals("Movis", currentInvalidEntity.getName());
        Assertions.assertNull(currentInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, currentInvalidEntity.isActive());

        final var anUpdatedCategory = Category.with(aCategory)
                .update(expectedName, expectedDescription, expectedIsActive);

        final var currentCategory = categoryGateway.update(anUpdatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), currentCategory.getId());
        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), currentCategory.getDeletedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());

        final var currentEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), currentEntity.getId());
        Assertions.assertEquals(expectedName, currentEntity.getName());
        Assertions.assertEquals(expectedDescription, currentEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, currentEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), currentEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(currentCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), currentEntity.getDeletedAt());
        Assertions.assertNull(currentEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("Movies", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("invalidId"));

        Assertions.assertEquals(0, categoryRepository.count());
    }

}