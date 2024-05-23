package com.studies.catalog.admin.e2e.genre;

import com.studies.catalog.admin.E2ETest;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.e2e.MockDsl;
import com.studies.catalog.admin.infrastructure.genre.models.UpdateGenreApiRequest;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void asACatalogAdminItShouldBeAbleToNavigateThroughAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Sports", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Sports", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1, "dra")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Sports", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var currentId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var currentGenre = retrieveAGenre(currentId);

        Assertions.assertEquals(expectedName, currentGenre.name());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.categories().size());
        Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(currentGenre.categories()));
        Assertions.assertEquals(expectedIsActive, currentGenre.active());
        Assertions.assertNotNull(currentGenre.createdAt());
        Assertions.assertNotNull(currentGenre.updatedAt());
        Assertions.assertNull(currentGenre.deletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest = get("/genres/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
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

    @Test
    void asACatalogAdminItShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var currentId = givenAGenre("action", expectedIsActive, expectedCategories);

        final var aRequestBody = new UpdateGenreApiRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        updateAGenre(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentGenre = genreRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var currentId = givenAGenre(expectedName, true, expectedCategories);

        final var aRequestBody = new UpdateGenreApiRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        updateAGenre(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentGenre = genreRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == currentGenre.getCategoryIDs().size());
        Assertions.assertTrue(expectedCategories.containsAll(currentGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNotNull(currentGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var currentId = givenAGenre(expectedName, false, expectedCategories);

        final var aRequestBody = new UpdateGenreApiRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        updateAGenre(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentGenre = genreRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategoryIDs());
        Assertions.assertEquals(expectedIsActive, currentGenre.isActive());
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());
    }

}