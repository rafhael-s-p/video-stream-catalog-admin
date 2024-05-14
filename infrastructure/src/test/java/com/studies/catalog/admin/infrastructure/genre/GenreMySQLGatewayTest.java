package com.studies.catalog.admin.infrastructure.genre;

import com.studies.catalog.admin.MySQLGatewayTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.infrastructure.category.CategoryMySQLGateway;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var currentGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), currentGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.getDeletedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var currentGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), currentGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.getDeletedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = Genre.newGenre("actn", expectedIsActive);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("actn", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        final var currentGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(currentGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("actn", expectedIsActive);
        aGenre.addCategories(List.of(movies.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("actn", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());

        final var currentGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        final var currentGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNull(currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAValidGenreActive_whenCallsUpdateGenreInactivate_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var currentGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNotNull(currentGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Action", true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // when
        genreGateway.deleteById(aGenre.getId());

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        // given
        Assertions.assertEquals(0, genreRepository.count());

        // when
        genreGateway.deleteById(GenreID.from("321"));

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // when
        final var currentGenre = genreGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(expectedId, currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(currentGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAnInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = GenreID.from("321");

        Assertions.assertEquals(0, genreRepository.count());

        // when
        final var currentGenre = genreGateway.findById(expectedId);

        // then
        Assertions.assertTrue(currentGenre.isEmpty());
    }

    @Test
    void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ac,0,10,1,1,Action",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comedy",
            "sci,0,10,1,1,Science",
            "terr,0,10,1,1,Terror",
    })
    void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());
        Assertions.assertEquals(expectedGenreName, currentPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Action",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comedy",
            "createdAt,desc,0,10,5,5,Science",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());
        Assertions.assertEquals(expectedGenreName, currentPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Action;Comedy",
            "1,2,2,5,Drama;Science",
            "2,2,1,5,Terror",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());

        int index = 0;
        for (final var expectedName : expectedGenres.split(";")) {
            final var currentName = currentPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, currentName);
            index++;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comedy", true)),
                GenreJpaEntity.from(Genre.newGenre("Action", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Science", true))
        ));
    }

    private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
        return expectedCategories.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }

}