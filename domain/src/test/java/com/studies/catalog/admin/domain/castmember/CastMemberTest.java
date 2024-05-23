package com.studies.catalog.admin.domain.castmember;

import com.studies.catalog.admin.domain.exceptions.NotificationException;
import helpers.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CastMemberTest extends TestHelper {

    @Test
    void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Angelina Jolie";
        final var expectedType = CastMemberType.ACTRESS;

        final var currentMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());
        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertNotNull(currentMember.getCreatedAt());
        Assertions.assertNotNull(currentMember.getUpdatedAt());
        Assertions.assertEquals(currentMember.getCreatedAt(), currentMember.getUpdatedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTRESS;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTRESS;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameWithLengthGreaterThan255_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = faker.lorem().fixedString(257);
        final var expectedType = CastMemberType.ACTRESS;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "Angelina Jolie";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

}