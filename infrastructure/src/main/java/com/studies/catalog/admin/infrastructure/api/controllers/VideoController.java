package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.video.create.CreateVideoInput;
import com.studies.catalog.admin.application.video.create.CreateVideoUseCase;
import com.studies.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.studies.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.studies.catalog.admin.application.video.update.UpdateVideoInput;
import com.studies.catalog.admin.application.video.update.UpdateVideoUseCase;
import com.studies.catalog.admin.domain.video.Resource;
import com.studies.catalog.admin.infrastructure.api.VideoAPI;
import com.studies.catalog.admin.infrastructure.utils.HashingUtils;
import com.studies.catalog.admin.infrastructure.video.models.CreateVideoApiRequest;
import com.studies.catalog.admin.infrastructure.video.models.UpdateVideoApiRequest;
import com.studies.catalog.admin.infrastructure.video.models.VideoApiResponse;
import com.studies.catalog.admin.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final CreateVideoUseCase createVideoUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoController(final GetVideoByIdUseCase getVideoByIdUseCase,
                           final CreateVideoUseCase createVideoUseCase,
                           final UpdateVideoUseCase updateVideoUseCase,
                           final DeleteVideoUseCase deleteVideoUseCase) {
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
    }

    @Override
    public VideoApiResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var anInput = CreateVideoInput.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );

        final var output = this.createVideoUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoApiRequest payload) {
        final var anInput = CreateVideoInput.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoApiRequest payload) {
        final var anInput = UpdateVideoInput.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.updateVideoUseCase.execute(anInput);

        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        try {
            return Resource.with(
                    part.getBytes(),
                    HashingUtils.checksum(part.getBytes()),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}