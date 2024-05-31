package com.studies.catalog.admin.infrastructure.castmember.models;

import com.studies.catalog.admin.Fixture;
import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
class CastMemberListApiResponseTest {

    @Autowired
    private JacksonTester<CastMemberListApiResponse> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type().name();
        final var expectedCreatedAt = Instant.now().toString();

        final var response = new CastMemberListApiResponse(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt
        );

        final var currentJson = this.json.write(response);

        Assertions.assertThat(currentJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt);
    }
}