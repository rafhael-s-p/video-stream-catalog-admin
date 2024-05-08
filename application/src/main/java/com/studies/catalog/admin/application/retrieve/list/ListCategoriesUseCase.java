package com.studies.catalog.admin.application.retrieve.list;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {}