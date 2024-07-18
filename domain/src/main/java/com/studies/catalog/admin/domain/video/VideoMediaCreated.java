package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.events.DomainEvent;
import com.studies.catalog.admin.domain.utils.InstantUtils;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }

}