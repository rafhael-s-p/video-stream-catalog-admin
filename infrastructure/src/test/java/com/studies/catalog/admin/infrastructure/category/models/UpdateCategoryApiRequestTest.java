package com.studies.catalog.admin.infrastructure.category.models;

import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class UpdateCategoryApiRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryApiRequest> json;

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