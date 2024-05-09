package com.studies.catalog.admin.application.genre.create;

import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateGenreUseCaseTest {

    @InjectMocks
    private CreateGenreUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Test
    void givenAValidInput_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInputWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInputWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Terror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())
        ));
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
        final var currentException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

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
        final var currentException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAValidInput_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var movies = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentaries = CategoryID.from("789");

        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentaries);

        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var movies = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentaries = CategoryID.from("789");

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var anInput =
                CreateGenreInput.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var currentException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

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