package com.studies.catalog.admin.application.video.delete;

import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DeleteVideoUseCaseImpl extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DeleteVideoUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.videoGateway.deleteById(VideoID.from(anIn));
    }

}