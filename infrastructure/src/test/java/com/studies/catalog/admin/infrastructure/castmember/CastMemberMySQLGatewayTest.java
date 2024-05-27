package com.studies.catalog.admin.infrastructure.castmember;

import com.studies.catalog.admin.MySQLGatewayTest;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.studies.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.studies.catalog.admin.Fixture.CastMember.type;
import static com.studies.catalog.admin.Fixture.name;

@MySQLGatewayTest
class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // when
        final var currentMember = castMemberGateway.create(CastMember.with(aMember));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, currentMember.getId());
        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), currentMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), currentMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        // given
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Bread", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final var savedMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals("Bread", savedMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, savedMember.getType());

        // when
        final var currentMember = castMemberGateway.update(
                CastMember.with(aMember).update(expectedName, expectedType)
        );

        // then
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, currentMember.getId());
        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), currentMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(currentMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        castMemberGateway.deleteById(aMember.getId());

        // then
        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
        // given
        final var aMember = CastMember.newMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        castMemberGateway.deleteById(CastMemberID.from("123"));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());
    }

}