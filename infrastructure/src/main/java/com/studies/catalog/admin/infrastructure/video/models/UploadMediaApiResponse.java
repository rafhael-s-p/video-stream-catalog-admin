package com.studies.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studies.catalog.admin.domain.video.VideoMediaType;

public record UploadMediaApiResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}