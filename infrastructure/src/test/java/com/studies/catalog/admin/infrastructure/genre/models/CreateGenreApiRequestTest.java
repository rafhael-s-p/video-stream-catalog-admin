package com.studies.catalog.admin.infrastructure.genre.models;

import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
class CreateGenreApiRequestTest {

    @Autowired
    private JacksonTester<CreateGenreApiRequest> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var request =
                new CreateGenreApiRequest(expectedName, expectedCategories, expectedIsActive);

        final var currentJson = this.json.write(request);

        Assertions.assertThat(currentJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedCategory = "123";
        final var expectedIsActive = true;

        final var json = """
                {
                  "name": "%s",
                  "categories_id": ["%s"],
                  "is_active": %s
                }  
                """.formatted(expectedName, expectedCategory, expectedIsActive);

        final var currentJson = this.json.parse(json);

        Assertions.assertThat(currentJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

}