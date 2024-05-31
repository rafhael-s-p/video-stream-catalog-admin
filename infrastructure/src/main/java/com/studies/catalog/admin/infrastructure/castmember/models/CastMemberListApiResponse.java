package com.studies.catalog.admin.infrastructure.castmember.models;

public record CastMemberListApiResponse(
        String id,
        String name,
        String type,
        String createdAt
) {
}