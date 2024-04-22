package com.studies.catalog.admin.application.create;

import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        aCategory.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }

}
