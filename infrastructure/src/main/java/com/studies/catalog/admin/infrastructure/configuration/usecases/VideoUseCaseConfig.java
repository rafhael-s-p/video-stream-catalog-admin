package com.studies.catalog.admin.infrastructure.configuration.usecases;

import com.studies.catalog.admin.application.video.create.CreateVideoUseCase;
import com.studies.catalog.admin.application.video.create.CreateVideoUseCaseImpl;
import com.studies.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.studies.catalog.admin.application.video.delete.DeleteVideoUseCaseImpl;
import com.studies.catalog.admin.application.video.media.get.GetMediaUseCase;
import com.studies.catalog.admin.application.video.media.get.GetMediaUseCaseImpl;
import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.studies.catalog.admin.application.video.media.update.UpdateMediaStatusUseCaseImpl;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaUseCaseImpl;
import com.studies.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.studies.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCaseImpl;
import com.studies.catalog.admin.application.video.retrieve.list.ListVideosUseCase;
import com.studies.catalog.admin.application.video.retrieve.list.ListVideosUseCaseImpl;
import com.studies.catalog.admin.application.video.update.UpdateVideoUseCase;
import com.studies.catalog.admin.application.video.update.UpdateVideoUseCaseImpl;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.video.MediaResourceGateway;
import com.studies.catalog.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(final CategoryGateway categoryGateway,
                              final CastMemberGateway castMemberGateway,
                              final GenreGateway genreGateway,
                              final MediaResourceGateway mediaResourceGateway,
                              final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new ListVideosUseCaseImpl(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new GetMediaUseCaseImpl(mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new GetVideoByIdUseCaseImpl(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new CreateVideoUseCaseImpl(categoryGateway, castMemberGateway, genreGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new UpdateVideoUseCaseImpl(videoGateway, categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DeleteVideoUseCaseImpl(videoGateway, mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new UploadMediaUseCaseImpl(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new UpdateMediaStatusUseCaseImpl(videoGateway);
    }

}