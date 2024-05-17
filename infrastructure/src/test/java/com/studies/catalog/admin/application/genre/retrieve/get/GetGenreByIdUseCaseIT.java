package com.studies.catalog.admin.application.genre.retrieve.get;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(series.getId(), movies.getId());

        final var aGenre = genreGateway.create(
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories)
        );

        final var expectedId = aGenre.getId();

        // when
        final var currentGenre = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), currentGenre.id());
        Assertions.assertEquals(expectedName, currentGenre.name());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.categories().size());
        Assertions.assertTrue(asString(expectedCategories).containsAll(currentGenre.categories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), currentGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.deletedAt());
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var expectedId = GenreID.from("123");

        // when
        final var currentException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}