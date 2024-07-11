package com.studies.catalog.admin.application.video.media.upload;

import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.video.MediaResourceGateway;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class UploadMediaUseCaseImpl extends UploadMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public UploadMediaUseCaseImpl(
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaInput anInput) {
        final var anId = VideoID.from(anInput.videoId());
        final var aResource = anInput.videoResource();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        switch (aResource.type()) {
            case VIDEO -> aVideo.setVideo(mediaResourceGateway.storeVideo(anId, aResource));
            case TRAILER -> aVideo.setTrailer(mediaResourceGateway.storeVideo(anId, aResource));
            case BANNER -> aVideo.setBanner(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.setThumbnail(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(mediaResourceGateway.storeImage(anId, aResource));
        }

        return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.type());
    }

    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }

}