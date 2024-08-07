package com.studies.catalog.admin.domain.castmember;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.UnitTest;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CastMemberTest extends UnitTest {

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
        final var expectedName = Fixture.description258();
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

    @Test
    void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "Brad Pitt";
        final var expectedType = CastMemberType.ACTOR;

        final var currentMember =
                CastMember.newMember("Bread", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());

        final var currentID = currentMember.getId();
        final var currentCreatedAt = currentMember.getCreatedAt();
        final var currentUpdatedAt = currentMember.getUpdatedAt();

        currentMember.update(expectedName, expectedType);

        Assertions.assertEquals(currentID, currentMember.getId());
        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertEquals(currentCreatedAt, currentMember.getCreatedAt());
        Assertions.assertTrue(currentUpdatedAt.isBefore(currentMember.getUpdatedAt()));
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var currentMember =
                CastMember.newMember("Bread", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> currentMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var currentMember =
                CastMember.newMember("Bread", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> currentMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithLengthGreaterThan255_shouldReceiveNotification() {
        final var expectedName = Fixture.description258();
        ;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var currentMember =
                CastMember.newMember("Bread", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());

         final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> currentMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
        final var expectedName = "Brad Pitt";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var currentMember =
                CastMember.newMember("Bread", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(currentMember);
        Assertions.assertNotNull(currentMember.getId());

        final var currentException = Assertions.assertThrows(
                NotificationException.class,
                () -> currentMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());
    }

}