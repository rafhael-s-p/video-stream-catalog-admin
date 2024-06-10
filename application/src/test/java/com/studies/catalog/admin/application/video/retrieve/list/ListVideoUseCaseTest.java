package com.studies.catalog.admin.application.video.retrieve.list;

import com.studies.catalog.admin.application.Fixture;
import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.application.genre.retrieve.list.GenreListOutput;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoSearchQuery;
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

class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListVideosUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        // given
        final var videos = List.of(
                Fixture.video(),
                Fixture.video()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndResultIsEmpty_shouldReturnGenres() {
        // given
        final var videos = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndGatewayThrowsRandomError_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, currentOutput.getMessage());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

}