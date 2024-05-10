package com.studies.catalog.admin.application.genre.retrieve.get;

import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;

import java.util.Objects;

public class GetGenreByIdUseCaseImpl extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public GetGenreByIdUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anId) {
        final var aGenreId = GenreID.from(anId);

        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }

}