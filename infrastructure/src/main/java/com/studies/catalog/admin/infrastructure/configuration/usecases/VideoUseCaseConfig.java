package com.studies.catalog.admin.infrastructure.configuration.usecases;

import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusUseCaseImpl;
import com.studies.catalog.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new UpdateMediaStatusUseCaseImpl(videoGateway);
    }

}