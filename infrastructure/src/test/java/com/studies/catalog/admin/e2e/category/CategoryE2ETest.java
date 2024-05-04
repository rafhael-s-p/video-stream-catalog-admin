package com.studies.catalog.admin.e2e.category;

import com.studies.catalog.admin.E2ETest;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiResponse;
import com.studies.catalog.admin.infrastructure.category.models.CreateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import com.studies.catalog.admin.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0-debian")
            .withPassword("1234")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Movies", null, true);
        givenACategory("Documentaries", null, true);
        givenACategory("Series", null, true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Series")));

        listCategories(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Movies", null, true);
        givenACategory("Documentaries", null, true);
        givenACategory("Series", null, true);

        listCategories(0, 1, "Ser")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Series")));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Movies", "B", true);
        givenACategory("Documentaries", "C", true);
        givenACategory("Series", "A", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentaries")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Movies")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Series")));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var currentCategory = retrieveACategory(currentId.getValue());

        Assertions.assertEquals(expectedName, currentCategory.name());
        Assertions.assertEquals(expectedDescription, currentCategory.description());
        Assertions.assertEquals(expectedIsActive, currentCategory.active());
        Assertions.assertNotNull(currentCategory.createdAt());
        Assertions.assertNotNull(currentCategory.updatedAt());
        Assertions.assertNull(currentCategory.deletedAt());
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(aRequest);
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryApiRequest(aName, aDescription, isActive);

        final var aRequest = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var currentId = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(currentId);
    }

    private CategoryApiResponse retrieveACategory(final String anId) throws Exception {

        final var aRequest = get("/categories/" + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, CategoryApiResponse.class);
    }

}
