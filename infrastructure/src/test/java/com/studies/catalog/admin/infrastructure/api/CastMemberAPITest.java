package com.studies.catalog.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.catalog.admin.ControllerTest;
import com.studies.catalog.admin.Fixture;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCaseImpl;
import com.studies.catalog.admin.application.castmember.delete.DeleteCastMemberUseCaseImpl;
import com.studies.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCaseImpl;
import com.studies.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCaseImpl;
import com.studies.catalog.admin.application.castmember.update.UpdateCastMemberUseCaseImpl;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCastMemberUseCaseImpl createCastMemberUseCase;

    @MockBean
    private DeleteCastMemberUseCaseImpl deleteCastMemberUseCase;

    @MockBean
    private GetCastMemberByIdUseCaseImpl getCastMemberByIdUseCase;

    @MockBean
    private ListCastMembersUseCaseImpl listCastMembersUseCase;

    @MockBean
    private UpdateCastMemberUseCaseImpl updateCastMemberUseCase;

    @Test
    void givenAValidInput_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.from("2c2c2d54d");

        final var anInput =
                new CreateCastMemberApiRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        // when
        final var aRequest = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(createCastMemberUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateCastMemberApiRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var aRequest = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

}