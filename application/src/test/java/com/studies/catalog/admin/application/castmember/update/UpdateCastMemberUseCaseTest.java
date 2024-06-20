package com.studies.catalog.admin.application.castmember.update;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UpdateCastMemberUseCaseImpl useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidInput_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        final var aMember = CastMember.newMember("mel gibson", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var anInput = UpdateCastMemberInput.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertEquals(expectedId.getValue(), currentOutput.id());

        verify(castMemberGateway).findById(eq(expectedId));

        verify(castMemberGateway).update(argThat(anUpdatedMember ->
                Objects.equals(expectedId, anUpdatedMember.getId())
                        && Objects.equals(expectedName, anUpdatedMember.getName())
                        && Objects.equals(expectedType, anUpdatedMember.getType())
                        && Objects.equals(aMember.getCreatedAt(), anUpdatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore(anUpdatedMember.getUpdatedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newMember("mel gibson", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput = UpdateCastMemberInput.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newMember("mel gibson", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var anInput = UpdateCastMemberInput.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);

        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var anInput = UpdateCastMemberInput.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var currentException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);

        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

}