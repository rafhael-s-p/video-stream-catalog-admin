package com.studies.catalog.admin.e2e.genre;

import com.studies.catalog.admin.E2ETest;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.e2e.MockDsl;
import com.studies.catalog.admin.infrastructure.category.models.CreateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.configuration.json.Json;
import com.studies.catalog.admin.infrastructure.genre.models.CreateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0-debian")
            .withPassword("1234")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    void asACatalogAdminItShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var currentId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var currentGenre = genreRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var currentId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var currentGenre = genreRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

}