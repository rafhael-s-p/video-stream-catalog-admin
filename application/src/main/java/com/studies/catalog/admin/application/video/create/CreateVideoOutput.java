package com.studies.catalog.admin.application.video.create;

import com.studies.catalog.admin.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }

}