package com.studies.catalog.admin.application.genre.retrieve.list;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListGenreUseCaseImpl useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        // given
        final var genres = List.of(
                Genre.newGenre("Fiction", true),
                Genre.newGenre("Funny", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "F";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        when(genreGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());

        Mockito.verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        // given
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "F";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        when(genreGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());

        Mockito.verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "F";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(genreGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, currentOutput.getMessage());

        Mockito.verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

}