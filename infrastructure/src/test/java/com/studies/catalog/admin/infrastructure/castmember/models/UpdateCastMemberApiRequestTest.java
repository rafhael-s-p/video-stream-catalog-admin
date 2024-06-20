package com.studies.catalog.admin.infrastructure.castmember.models;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class UpdateCastMemberApiRequestTest {

    @Autowired
    private JacksonTester<UpdateCastMemberApiRequest> json;

    @Test
    void testUnmarshall() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var json = """
                {
                  "name": "%s",
                  "type": "%s"
                }
                """.formatted(expectedName, expectedType);

        final var currentJson = this.json.parse(json);

        Assertions.assertThat(currentJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}