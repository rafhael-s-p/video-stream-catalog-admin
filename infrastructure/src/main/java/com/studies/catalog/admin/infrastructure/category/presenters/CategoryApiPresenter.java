package com.studies.catalog.admin.infrastructure.category.presenters;

import com.studies.catalog.admin.application.retrieve.get.CategoryOutput;
import com.studies.catalog.admin.application.retrieve.list.CategoryListOutput;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiResponse;
import com.studies.catalog.admin.infrastructure.category.models.CategoryListApiResponse;

public interface CategoryApiPresenter {

    static CategoryListApiResponse present(final CategoryListOutput output) {
        return new CategoryListApiResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

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