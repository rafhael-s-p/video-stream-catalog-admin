package com.studies.catalog.admin.application.category.retrieve.list;

import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.domain.pagination.Pagination;

import java.util.Objects;

public class ListCategoriesUseCaseImpl extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public ListCategoriesUseCaseImpl(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final SearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }

}
