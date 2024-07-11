package com.studies.catalog.admin.application.video.media.update;

import com.studies.catalog.admin.domain.video.MediaStatus;

public record UpdateMediaStatusInput(
        MediaStatus status,
        String videoId,
        String resourceId,
        String folder,
        String filename
) {

    public static UpdateMediaStatusInput with(
            final MediaStatus status,
            final String videoId,
            final String resourceId,
            final String folder,
            final String filename
    ) {
        return new UpdateMediaStatusInput(status, videoId, resourceId, folder, filename);
    }

}