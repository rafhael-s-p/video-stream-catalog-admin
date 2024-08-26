package com.studies.catalog.admin.application.genre.create;

import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.validation.ValidationHandler;
import com.studies.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateGenreUseCaseImpl extends CreateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public CreateGenreUseCaseImpl(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreInput anInput) {
        final var aName = anInput.name();
        final var isActive = anInput.isActive();
        final var categories = toCategoryID(anInput.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));

        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasError())
            throw new NotificationException("Could not create an Aggregate Genre", notification);

        aGenre.addCategories(categories);

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
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

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
