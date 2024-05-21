package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.genre.create.CreateGenreInput;
import com.studies.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.infrastructure.api.GenreAPI;
import com.studies.catalog.admin.infrastructure.genre.models.CreateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.models.GenreApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.GenreListApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.UpdateGenreApiRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;

    public GenreController(final CreateGenreUseCase createGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
    }

    @Override
    public Pagination<GenreListApiResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return null;
    }

    @Override
    public GenreApiResponse getById(final String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreApiRequest input) {
        final var anInput = CreateGenreInput.with(
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreApiRequest input) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }

}