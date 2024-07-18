package com.studies.catalog.admin.application.video.create;

import com.studies.catalog.admin.domain.Identifier;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.InternalErrorException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.validation.ValidationHandler;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import com.studies.catalog.admin.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.studies.catalog.admin.domain.video.VideoMediaType.*;

public class CreateVideoUseCaseImpl extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public CreateVideoUseCaseImpl(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoInput anInput) {
        final var aRating = Rating.of(anInput.rating()).orElse(null);
        final var aLaunchYear = anInput.launchedAt() != null ? Year.of(anInput.launchedAt()) : null;
        final var categories = toIdentifier(anInput.categories(), CategoryID::from);
        final var genres = toIdentifier(anInput.genres(), GenreID::from);
        final var members = toIdentifier(anInput.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        final var aVideo = Video.newVideo(
                anInput.title(),
                anInput.description(),
                aLaunchYear,
                anInput.duration(),
                anInput.opened(),
                anInput.published(),
                aRating,
                categories,
                genres,
                members
        );

        aVideo.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not create Aggregate Video", notification);
        }

        return CreateVideoOutput.from(create(anInput, aVideo));
    }

    private Video create(final CreateVideoInput anInput, final Video aVideo) {
        final var anId = aVideo.getId();

        try {
            final var aVideoMedia = anInput.getVideo()
                    .map(it -> this.mediaResourceGateway.storeVideo(anId, VideoResource.with(VIDEO, it)))
                    .orElse(null);

            final var aTrailerMedia = anInput.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeVideo(anId, VideoResource.with(TRAILER, it)))
                    .orElse(null);

            final var aBannerMedia = anInput.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(BANNER, it)))
                    .orElse(null);

            final var aThumbnailMedia = anInput.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(THUMBNAIL, it)))
                    .orElse(null);

            final var aThumbHalfMedia = anInput.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(THUMBNAIL_HALF, it)))
                    .orElse(null);

            return this.videoGateway.create(
                    aVideo
                            .updateVideoMedia(aVideoMedia)
                            .updateTrailerMedia(aTrailerMedia)
                            .updateBannerMedia(aBannerMedia)
                            .updateThumbnailMedia(aThumbnailMedia)
                            .updateThumbnailHalfMedia(aThumbHalfMedia)
            );
        } catch (final Throwable throwable) {
            this.mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with(
                    "An error on create video was observed [videoId:%s]".formatted(anId.getValue()),
                    throwable
            );
        }
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

}