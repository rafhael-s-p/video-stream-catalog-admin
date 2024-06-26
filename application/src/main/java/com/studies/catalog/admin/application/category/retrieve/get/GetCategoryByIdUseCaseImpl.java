package com.studies.catalog.admin.application.category.retrieve.get;

import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class GetCategoryByIdUseCaseImpl extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public GetCategoryByIdUseCaseImpl(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        final var aCategoryID = CategoryID.from(anId);

        return this.categoryGateway.findById(aCategoryID)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(aCategoryID));
    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }

}
