package com.studies.catalog.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.catalog.admin.ControllerTest;
import com.studies.catalog.admin.application.genre.create.CreateGenreOutput;
import com.studies.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.studies.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.studies.catalog.admin.application.genre.retrieve.get.GenreOutput;
import com.studies.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.studies.catalog.admin.application.genre.update.UpdateGenreOutput;
import com.studies.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import com.studies.catalog.admin.infrastructure.genre.models.CreateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.models.UpdateGenreApiRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @Test
    void givenAValidInput_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var anInput =
                new CreateGenreApiRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        // when
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createGenreUseCase).execute(argThat(input ->
                Objects.equals(expectedName, input.name())
                        && Objects.equals(expectedCategories, input.categories())
                        && Objects.equals(expectedIsActive, input.isActive())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateGenreApiRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createGenreUseCase).execute(argThat(input ->
                Objects.equals(expectedName, input.name())
                        && Objects.equals(expectedCategories, input.categories())
                        && Objects.equals(expectedIsActive, input.isActive())
        ));
    }

    @Test
    void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(
                        expectedCategories.stream()
                                .map(CategoryID::from)
                                .toList()
                );

        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any()))
                .thenReturn(GenreOutput.from(aGenre));

        // when
        final var aRequest = get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(getGenreByIdUseCase).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(getGenreByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        // when
        final var aRequest = get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    void givenAValidInput_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var anInput =
                new UpdateGenreApiRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(aGenre));

        // when
        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase).execute(argThat(input ->
                Objects.equals(expectedName, input.name())
                        && Objects.equals(expectedCategories, input.categories())
                        && Objects.equals(expectedIsActive, input.isActive())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aGenre = Genre.newGenre("Action", expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var anInput =
                new UpdateGenreApiRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase).execute(argThat(input ->
                Objects.equals(expectedName, input.name())
                        && Objects.equals(expectedCategories, input.categories())
                        && Objects.equals(expectedIsActive, input.isActive())
        ));
    }

    @Test
    void givenAValidId_whenCallsDeleteGenre_shouldBeOK() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteGenreUseCase).execute(any());

        // when
        final var aRequest = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(aRequest);

        // then
        result.andExpect(status().isNoContent());

        verify(deleteGenreUseCase).execute(eq(expectedId));
    }

}