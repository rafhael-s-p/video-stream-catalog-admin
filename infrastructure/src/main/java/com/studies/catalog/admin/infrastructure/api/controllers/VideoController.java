package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.video.create.CreateVideoInput;
import com.studies.catalog.admin.application.video.create.CreateVideoUseCase;
import com.studies.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.studies.catalog.admin.application.video.media.get.GetMediaInput;
import com.studies.catalog.admin.application.video.media.get.GetMediaUseCase;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaInput;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import com.studies.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.studies.catalog.admin.application.video.retrieve.list.ListVideosUseCase;
import com.studies.catalog.admin.application.video.update.UpdateVideoInput;
import com.studies.catalog.admin.application.video.update.UpdateVideoUseCase;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.video.Resource;
import com.studies.catalog.admin.domain.video.VideoMediaType;
import com.studies.catalog.admin.domain.video.VideoResource;
import com.studies.catalog.admin.domain.video.VideoSearchQuery;
import com.studies.catalog.admin.infrastructure.api.VideoAPI;
import com.studies.catalog.admin.infrastructure.utils.HashingUtils;
import com.studies.catalog.admin.infrastructure.video.models.CreateVideoApiRequest;
import com.studies.catalog.admin.infrastructure.video.models.UpdateVideoApiRequest;
import com.studies.catalog.admin.infrastructure.video.models.VideoApiResponse;
import com.studies.catalog.admin.infrastructure.video.models.VideoListApiResponse;
import com.studies.catalog.admin.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.studies.catalog.admin.domain.utils.CollectionUtils.mapTo;

@RestController
public class VideoController implements VideoAPI {

    private final ListVideosUseCase listVideosUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final CreateVideoUseCase createVideoUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(final ListVideosUseCase listVideosUseCase,
                           final GetVideoByIdUseCase getVideoByIdUseCase,
                           final GetMediaUseCase getMediaUseCase,
                           final CreateVideoUseCase createVideoUseCase,
                           final UpdateVideoUseCase updateVideoUseCase,
                           final DeleteVideoUseCase deleteVideoUseCase,
                           final UploadMediaUseCase uploadMediaUseCase) {
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListApiResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        final var castMemberIDs = mapTo(castMembers, CastMemberID::from);
        final var categoriesIDs = mapTo(categories, CategoryID::from);
        final var genresIDs = mapTo(genres, GenreID::from);

        final var aQuery =
                new VideoSearchQuery(page, perPage, search, sort, direction, castMemberIDs, categoriesIDs, genresIDs);

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public VideoApiResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var aMedia =
                this.getMediaUseCase.execute(GetMediaInput.with(id, type));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
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

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var aType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(type))));

        final var anInput =
                UploadMediaInput.with(id, VideoResource.with(aType, resourceOf(media)));

        final var output = this.uploadMediaUseCase.execute(anInput);

        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(id, type)))
                .body(VideoApiPresenter.present(output));
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