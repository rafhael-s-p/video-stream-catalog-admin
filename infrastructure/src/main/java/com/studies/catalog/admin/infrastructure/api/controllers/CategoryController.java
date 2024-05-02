package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.create.CreateCategoryCommand;
import com.studies.catalog.admin.application.create.CreateCategoryOutput;
import com.studies.catalog.admin.application.create.CreateCategoryUseCase;
import com.studies.catalog.admin.application.retrieve.get.GetCategoryByIdUseCase;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import com.studies.catalog.admin.infrastructure.api.CategoryAPI;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiOutput;
import com.studies.catalog.admin.infrastructure.category.models.CreateCategoryApiInput;
import com.studies.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase
                .execute(aCommand)
                .fold(onError, onSuccess);
    }

}