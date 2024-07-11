package com.studies.catalog.admin.application.video.media.get;

import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.video.MediaResourceGateway;
import com.studies.catalog.admin.domain.video.VideoID;
import com.studies.catalog.admin.domain.video.VideoMediaType;

import java.util.Objects;

public class GetMediaUseCaseImpl extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public GetMediaUseCaseImpl(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaInput anInput) {
        final var anId = VideoID.from(anInput.videoId());
        final var aType = VideoMediaType.of(anInput.mediaType())
                .orElseThrow(() -> typeNotFound(anInput.mediaType()));

        final var aResource =
                this.mediaResourceGateway.getResource(anId, aType)
                        .orElseThrow(() -> notFound(anInput.videoId(), anInput.mediaType()));

        return MediaOutput.with(aResource);
    }

    private NotFoundException notFound(final String anId, final String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }

    private NotFoundException typeNotFound(final String aType) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
    }

}
