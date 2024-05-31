package com.studies.catalog.admin.infrastructure.castmember.models;

public record CastMemberApiResponse(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {
}