package com.studies.catalog.admin.infrastructure.video.presenters;

import com.studies.catalog.admin.application.video.media.upload.UploadMediaOutput;
import com.studies.catalog.admin.application.video.retrieve.get.VideoOutput;
import com.studies.catalog.admin.application.video.retrieve.list.VideoListOutput;
import com.studies.catalog.admin.application.video.update.UpdateVideoOutput;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.video.ImageMedia;
import com.studies.catalog.admin.domain.video.VideoMedia;
import com.studies.catalog.admin.infrastructure.video.models.*;

public interface VideoApiPresenter {

    static VideoApiResponse present(final VideoOutput output) {
        return new VideoApiResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static VideoMediaApiResponse present(final VideoMedia media) {
        if (media == null) {
            return null;
        }
        return new VideoMediaApiResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static ImageMediaApiResponse present(final ImageMedia image) {
        if (image == null) {
            return null;
        }
        return new ImageMediaApiResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }

    static UpdateVideoApiResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoApiResponse(output.id());
    }

    static VideoListApiResponse present(final VideoListOutput output) {
        return new VideoListApiResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static Pagination<VideoListApiResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoApiPresenter::present);
    }

    static UploadMediaApiResponse present(final UploadMediaOutput output) {
        return new UploadMediaApiResponse(output.videoId(), output.mediaType());
    }

}