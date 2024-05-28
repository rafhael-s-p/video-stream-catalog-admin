package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.Fixture;
import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidInput_whenCallsCreateCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var anInput = CreateCastMemberInput.with(expectedName, expectedType);

        // when
        final var currentOutput = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        final var currentMember = this.castMemberRepository.findById(currentOutput.id()).get();

        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertNotNull(currentMember.getCreatedAt());
        Assertions.assertNotNull(currentMember.getUpdatedAt());
        Assertions.assertEquals(currentMember.getCreatedAt(), currentMember.getUpdatedAt());

        verify(castMemberGateway).create(any());
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