package com.studies.catalog.admin.application.castmember.retrieve.get;

import com.studies.catalog.admin.application.Fixture;
import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetCastMemberByIdUseCaseImpl useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // when
        final var currentOutput = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());
        Assertions.assertEquals(expectedName, currentOutput.name());
        Assertions.assertEquals(expectedType, currentOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), currentOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), currentOutput.updatedAt());

        verify(castMemberGateway).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123");

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var currentOutput = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedErrorMessage, currentOutput.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }

}