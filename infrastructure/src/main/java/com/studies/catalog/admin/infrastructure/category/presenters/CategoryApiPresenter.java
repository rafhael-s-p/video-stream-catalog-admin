package com.studies.catalog.admin.infrastructure.category.presenters;

import com.studies.catalog.admin.application.retrieve.get.CategoryOutput;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiResponse;

public interface CategoryApiPresenter {

    static CategoryApiResponse present(final CategoryOutput output) {
        return new CategoryApiResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

}