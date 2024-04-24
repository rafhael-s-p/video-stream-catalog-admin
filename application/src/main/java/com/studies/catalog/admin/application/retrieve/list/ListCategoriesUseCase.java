package com.studies.catalog.admin.application.retrieve.list;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.category.CategorySearchQuery;
import com.studies.catalog.admin.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {}