package com.studies.catalog.admin.application.genre.update;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UpdateGenreUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    void givenAValidInput_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("Nrror", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInputWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("Nrror", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInputWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("Nrror", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Terror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(anUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        final var aGenre = Genre.newGenre("Nrror", true);

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

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        // when
        final var currentException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());

        Mockito.verify(genreGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesThatDoesNotExists_shouldReturnNotificationException() {
        // given
        final var movies = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var aGenre = Genre.newGenre("Nrror", true);

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentaries);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var anInput = UpdateGenreInput.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(movies));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(anInput);
        });

        // then
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, currentException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, currentException.getErrors().get(1).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

        Mockito.verify(genreGateway, times(0)).update(any());
    }

}
