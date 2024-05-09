package com.studies.catalog.admin.application.genre.delete;

import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;

import java.util.Objects;

public class DeleteGenreUseCaseImpl extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DeleteGenreUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }

}