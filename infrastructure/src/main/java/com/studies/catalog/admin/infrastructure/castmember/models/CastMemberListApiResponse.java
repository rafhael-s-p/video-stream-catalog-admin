package com.studies.catalog.admin.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CastMemberListApiResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") String createdAt
) {
}