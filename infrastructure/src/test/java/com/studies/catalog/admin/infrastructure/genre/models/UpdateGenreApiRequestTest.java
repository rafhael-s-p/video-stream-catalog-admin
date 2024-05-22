package com.studies.catalog.admin.infrastructure.genre.models;

import com.studies.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
class UpdateGenreApiRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreApiRequest> json;

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