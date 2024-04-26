package com.studies.catalog.admin.infrastructure.configuration.usecases;

import com.studies.catalog.admin.application.create.CreateCategoryUseCase;
import com.studies.catalog.admin.application.create.CreateCategoryUseCaseImpl;
import com.studies.catalog.admin.application.delete.DeleteCategoryUseCase;
import com.studies.catalog.admin.application.delete.DeleteCategoryUseCaseImpl;
import com.studies.catalog.admin.application.retrieve.get.GetCategoryByIdUseCase;
import com.studies.catalog.admin.application.retrieve.get.GetCategoryByIdUseCaseImpl;
import com.studies.catalog.admin.application.retrieve.list.ListCategoriesUseCase;
import com.studies.catalog.admin.application.retrieve.list.ListCategoriesUseCaseImpl;
import com.studies.catalog.admin.application.update.UpdateCategoryUseCase;
import com.studies.catalog.admin.application.update.UpdateCategoryUseCaseImpl;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new ListCategoriesUseCaseImpl(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new GetCategoryByIdUseCaseImpl(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new CreateCategoryUseCaseImpl(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new UpdateCategoryUseCaseImpl(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DeleteCategoryUseCaseImpl(categoryGateway);
    }

}
