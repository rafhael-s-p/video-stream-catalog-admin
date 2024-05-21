package com.studies.catalog.admin.infrastructure.genre.presenters;

import com.studies.catalog.admin.application.genre.retrieve.get.GenreOutput;
import com.studies.catalog.admin.application.genre.retrieve.list.GenreListOutput;
import com.studies.catalog.admin.infrastructure.genre.models.GenreApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.GenreListApiResponse;

public interface GenreApiPresenter {

    static GenreApiResponse present(final GenreOutput output) {
        return new GenreApiResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListApiResponse present(final GenreListOutput output) {
        return new GenreListApiResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

}