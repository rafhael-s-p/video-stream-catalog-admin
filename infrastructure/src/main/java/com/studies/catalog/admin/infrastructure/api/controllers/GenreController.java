package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.genre.create.CreateGenreInput;
import com.studies.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.studies.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.studies.catalog.admin.application.genre.update.UpdateGenreInput;
import com.studies.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.infrastructure.api.GenreAPI;
import com.studies.catalog.admin.infrastructure.genre.models.CreateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.models.GenreApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.GenreListApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.UpdateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final CreateGenreUseCase createGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(final GetGenreByIdUseCase getGenreByIdUseCase,
                           final CreateGenreUseCase createGenreUseCase,
                           final UpdateGenreUseCase updateGenreUseCase) {
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.createGenreUseCase = createGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
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
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
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
        final var anInput = UpdateGenreInput.with(
                id,
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.updateGenreUseCase.execute(anInput);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {

    }

}