package com.studies.catalog.admin.infrastructure.video;

import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoGateway;
import com.studies.catalog.admin.domain.video.VideoID;
import com.studies.catalog.admin.domain.video.VideoSearchQuery;
import com.studies.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.studies.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(final VideoRepository videoRepository) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return null;
    }

    @Override
    public Optional<Video> findById(VideoID anId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();
    }

    @Override
    public Video update(Video aVideo) {
        return null;
    }

    @Override
    public void deleteById(VideoID anId) {

    }

}