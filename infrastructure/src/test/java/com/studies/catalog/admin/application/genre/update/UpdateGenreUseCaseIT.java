package com.studies.catalog.admin.application.genre.update;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@IntegrationTest
class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidInput_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("actonn", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        final var currentGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidInputWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var aGenre = genreGateway.create(Genre.newGenre("actonn", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        final var currentGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("actonn", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        final var currentGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(currentGenre.getUpdatedAt()));
        Assertions.assertNotNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("actonn", true));

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());

        Mockito.verify(genreGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var aGenre = genreGateway.create(Genre.newGenre("actonn", true));

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series, documentaries);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, currentException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, currentException.getErrors().get(1).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

        Mockito.verify(genreGateway, times(0)).update(any());
    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}