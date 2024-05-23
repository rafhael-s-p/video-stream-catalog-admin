package com.studies.catalog.admin.e2e.category;

import com.studies.catalog.admin.E2ETest;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.e2e.MockDsl;
import com.studies.catalog.admin.infrastructure.category.models.UpdateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.category.persistence.CategoryRepository;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

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

    @Override
    public MockMvc mvc() {
        return this.mvc;
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
    void asACatalogAdminItShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var currentCategory = retrieveACategory(currentId);

        Assertions.assertEquals(expectedName, currentCategory.name());
        Assertions.assertEquals(expectedDescription, currentCategory.description());
        Assertions.assertEquals(expectedIsActive, currentCategory.active());
        Assertions.assertNotNull(currentCategory.createdAt());
        Assertions.assertNotNull(currentCategory.updatedAt());
        Assertions.assertNull(currentCategory.deletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var aRequest = get("/categories/321")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 321 was not found")));
    }

    @Test
    void asACatalogAdminItShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var currentCategory = categoryRepository.findById(currentId.getValue()).get();
        ;

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var currentId = givenACategory("Series", null, true);

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentCategory = categoryRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var currentId = givenACategory(expectedName, expectedDescription, true);

        final var aRequestBody = new UpdateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentCategory = categoryRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNotNull(currentCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Series";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var currentId = givenACategory(expectedName, expectedDescription, false);

        final var aRequestBody = new UpdateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(currentId, aRequestBody)
                .andExpect(status().isOk());

        final var currentCategory = categoryRepository.findById(currentId.getValue()).get();

        Assertions.assertEquals(expectedName, currentCategory.getName());
        Assertions.assertEquals(expectedDescription, currentCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, currentCategory.isActive());
        Assertions.assertNotNull(currentCategory.getCreatedAt());
        Assertions.assertNotNull(currentCategory.getUpdatedAt());
        Assertions.assertNull(currentCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminItShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var currentId = givenACategory("Series", null, true);

        deleteACategory(currentId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.categoryRepository.existsById(currentId.getValue()));
    }

    @Test
    void asACatalogAdminItShouldNotSeeAnErrorByDeletingANotExistentCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        deleteACategory(CategoryID.from("12313"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, categoryRepository.count());
    }

}
