package com.studies.catalog.admin.infrastructure.category.models;

import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class CreateCategoryApiRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryApiRequest> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var request = new CreateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);

        final var currentJson = this.json.write(request);

        Assertions.assertThat(currentJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var json = """
                {
                  "name": "%s",
                  "description": "%s",
                  "is_active": %s
                }    
                """.formatted(expectedName, expectedDescription, expectedIsActive);

        final var currentJson = this.json.parse(json);

        Assertions.assertThat(currentJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

}