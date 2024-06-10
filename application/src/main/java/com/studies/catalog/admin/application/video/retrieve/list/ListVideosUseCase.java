package com.studies.catalog.admin.application.video.retrieve.list;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}