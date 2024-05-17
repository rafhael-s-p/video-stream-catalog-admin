package com.studies.catalog.admin.application.genre.create;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@IntegrationTest
class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidInput_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var movies =
                categoryGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentGenre = genreRepository.findById(currentOutput.id()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidInputWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentGenre = genreRepository.findById(currentOutput.id()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAValidInputWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentGenre = genreRepository.findById(currentOutput.id()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNotNull(currentGenre.getDeletedAt());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var movies = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series.getId(), documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var anInput =
                CreateGenreInput.with(expectName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, currentException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, currentException.getErrors().get(1).message());

        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}