package com.studies.catalog.admin.application.genre.retrieve.get;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetGenreByIdUseCaseImpl useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        final var expectedName = "Terror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("987"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(aGenre));

        // when
        final var currentGenre = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), currentGenre.id());
        Assertions.assertEquals(expectedName, currentGenre.name());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), currentGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), currentGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), currentGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), currentGenre.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Genre with ID 987 was not found";

        final var expectedId = GenreID.from("987");

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        // when
        final var currentException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());
    }

}