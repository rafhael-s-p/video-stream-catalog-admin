package com.studies.catalog.admin.infrastructure.configuration.usecases;

import com.studies.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.studies.catalog.admin.application.genre.create.CreateGenreUseCaseImpl;
import com.studies.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.studies.catalog.admin.application.genre.delete.DeleteGenreUseCaseImpl;
import com.studies.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.studies.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCaseImpl;
import com.studies.catalog.admin.application.genre.retrieve.list.ListGenreUseCase;
import com.studies.catalog.admin.application.genre.retrieve.list.ListGenreUseCaseImpl;
import com.studies.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.studies.catalog.admin.application.genre.update.UpdateGenreUseCaseImpl;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new ListGenreUseCaseImpl(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new GetGenreByIdUseCaseImpl(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new CreateGenreUseCaseImpl(categoryGateway, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new UpdateGenreUseCaseImpl(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DeleteGenreUseCaseImpl(genreGateway);
    }

}