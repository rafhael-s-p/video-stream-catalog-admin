package com.studies.catalog.admin.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}