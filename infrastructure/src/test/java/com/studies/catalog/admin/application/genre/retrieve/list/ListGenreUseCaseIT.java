package com.studies.catalog.admin.application.genre.retrieve.list;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        // given
        final var genres = List.of(
                Genre.newGenre("Action", true),
                Genre.newGenre("Adventure", true)
        );

        genreRepository.saveAllAndFlush(
                genres.stream()
                        .map(GenreJpaEntity::from)
                        .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertTrue(expectedItems.size() == currentOutput.items().size());
        Assertions.assertTrue(expectedItems.containsAll(currentOutput.items()));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        // given

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());
    }
}
