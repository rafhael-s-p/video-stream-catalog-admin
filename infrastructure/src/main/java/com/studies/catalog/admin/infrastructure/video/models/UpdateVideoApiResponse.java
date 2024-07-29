package com.studies.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoApiResponse(@JsonProperty("id") String id) {
}