package com.studies.catalog.admin.application.genre.update;

import com.studies.catalog.admin.domain.Identifier;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.validation.ValidationHandler;
import com.studies.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UpdateGenreUseCaseImpl extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public UpdateGenreUseCaseImpl(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreInput anInput) {
        final var anId = GenreID.from(anInput.id());
        final var aName = anInput.name();
        final var isActive = anInput.isActive();
        final var categories = toCategoryId(anInput.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasError())
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(anInput.id()), notification);

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty())
            return notification;

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryID> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}