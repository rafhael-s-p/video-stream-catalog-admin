package com.studies.catalog.admin.application.video.media.upload;

import com.studies.catalog.admin.domain.video.VideoResource;

public record UploadMediaInput(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaInput with(final String anId, final VideoResource aResource) {
        return new UploadMediaInput(anId, aResource);
    }

}