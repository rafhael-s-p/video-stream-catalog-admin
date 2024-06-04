package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Pagination<Video> findAll(VideoSearchQuery aQuery);

    Optional<Video> findById(VideoID anId);

    Video create(Video aVideo);

    Video update(Video aVideo);

    void deleteById(VideoID anId);

}