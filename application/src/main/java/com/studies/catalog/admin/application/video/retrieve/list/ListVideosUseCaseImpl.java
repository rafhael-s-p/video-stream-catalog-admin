package com.studies.catalog.admin.application.video.retrieve.list;

import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoSearchQuery;

import java.util.Objects;

public class ListVideosUseCaseImpl extends ListVideosUseCase {

    private final VideoGateway videoGateway;

    public ListVideosUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery)
                .map(VideoListOutput::from);
    }

}