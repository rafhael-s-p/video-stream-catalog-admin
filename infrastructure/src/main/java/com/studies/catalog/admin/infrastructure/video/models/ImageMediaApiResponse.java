package com.studies.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageMediaApiResponse(
        @JsonProperty("id") String id,
        @JsonProperty("checksum") String checksum,
        @JsonProperty("name") String name,
        @JsonProperty("location") String location
) {
}