package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.application.Fixture;
import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private CreateCastMemberUseCaseImpl useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidInput_whenCallsCreateCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var anInput = CreateCastMemberInput.with(expectedName, expectedType);

        when(castMemberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        verify(castMemberGateway).create(argThat(aMember ->
                Objects.nonNull(aMember.getId())
                        && Objects.equals(expectedName, aMember.getName())
                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput = CreateCastMemberInput.with(expectedName, expectedType);

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var anInput = CreateCastMemberInput.with(expectedName, expectedType);

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

}