package com.studies.catalog.admin.application.category.create;

import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class CreateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private CreateCategoryUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidInput_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var anInput = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var currentOutput = useCase.execute(anInput).get();

        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var anInput = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(anInput).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).create(any());
    }

    @Test
    void givenAValidInputWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var anInput = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var currentOutput = useCase.execute(anInput).get();

        Assertions.assertNotNull(currentOutput);
        Assertions.assertNotNull(currentOutput.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInput_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var anInput = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(anInput).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

}
