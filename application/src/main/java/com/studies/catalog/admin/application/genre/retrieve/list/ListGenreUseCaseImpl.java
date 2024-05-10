package com.studies.catalog.admin.application.genre.retrieve.list;

import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public class ListGenreUseCaseImpl extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    public ListGenreUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }

}