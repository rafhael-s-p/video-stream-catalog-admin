package com.studies.catalog.admin.application.genre.retrieve.list;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase
        extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}