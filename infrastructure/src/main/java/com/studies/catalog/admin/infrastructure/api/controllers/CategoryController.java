package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.category.create.CreateCategoryInput;
import com.studies.catalog.admin.application.category.create.CreateCategoryOutput;
import com.studies.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.studies.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.studies.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.studies.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.studies.catalog.admin.application.category.update.UpdateCategoryInput;
import com.studies.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.studies.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import com.studies.catalog.admin.infrastructure.api.CategoryAPI;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiResponse;
import com.studies.catalog.admin.infrastructure.category.models.CategoryListApiResponse;
import com.studies.catalog.admin.infrastructure.category.models.CreateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.category.models.UpdateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final ListCategoriesUseCase listCategoriesUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            final ListCategoriesUseCase listCategoriesUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final CreateCategoryUseCase createCategoryUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public Pagination<CategoryListApiResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryApiResponse getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiRequest input) {
        final var anInput = CreateCategoryInput.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase
                .execute(anInput)
                .fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiRequest input) {
        final var anInput = UpdateCategoryInput.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(anInput)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }

}