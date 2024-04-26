package com.studies.catalog.admin.infrastructure.category.persistence;

import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var aCategory = Category.newCategory("Movies", "The most watched category", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var currentException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var currentCause =
                Assertions.assertInstanceOf(PropertyValueException.class, currentException.getCause());

        Assertions.assertEquals(expectedPropertyName, currentCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, currentCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var aCategory = Category.newCategory("Movies", "The most watched category", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var currentException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var currentCause =
                Assertions.assertInstanceOf(PropertyValueException.class, currentException.getCause());

        Assertions.assertEquals(expectedPropertyName, currentCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, currentCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.studies.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var aCategory = Category.newCategory("Movies", "The most watched category", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var currentException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var currentCause =
                Assertions.assertInstanceOf(PropertyValueException.class, currentException.getCause());

        Assertions.assertEquals(expectedPropertyName, currentCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, currentCause.getMessage());
    }

}
