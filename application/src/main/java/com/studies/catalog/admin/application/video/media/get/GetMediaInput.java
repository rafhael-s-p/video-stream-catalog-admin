package com.studies.catalog.admin.application.video.media.get;

public record GetMediaInput(
        String videoId,
        String mediaType
) {

    public static GetMediaInput with(final String anId, final String aType) {
        return new GetMediaInput(anId, aType);
    }
}
