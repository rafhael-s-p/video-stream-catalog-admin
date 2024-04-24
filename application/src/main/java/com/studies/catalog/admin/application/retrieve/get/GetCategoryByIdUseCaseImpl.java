package com.studies.catalog.admin.application.retrieve.get;

import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.domain.validation.Error;

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

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }

}
