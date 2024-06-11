package com.studies.catalog.admin.infrastructure.video.persistence;

import com.studies.catalog.admin.domain.video.Rating;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Entity(name = "Video")
@Table(name = "videos")
public class VideoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "year_launched", nullable = false)
    private int yearLaunched;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private VideoMediaJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private VideoMediaJpaEntity trailer;

    public VideoJpaEntity() {
    }

    public VideoJpaEntity(
            final UUID id,
            final String title,
            final String description,
            final int yearLaunched,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final double duration,
            final Instant createdAt,
            final Instant updatedAt,
            final VideoMediaJpaEntity video,
            final VideoMediaJpaEntity trailer
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
    }

    public static VideoJpaEntity from(final Video aVideo) {
        return new VideoJpaEntity(
                UUID.fromString(aVideo.getId().getValue()),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo()
                        .map(VideoMediaJpaEntity::from)
                        .orElse(null),
                aVideo.getTrailer()
                        .map(VideoMediaJpaEntity::from)
                        .orElse(null)
        );
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYearLaunched()),
                getDuration(),
                isOpened(),
                isPublished(),
                getRating(),
                getCreatedAt(),
                getUpdatedAt(),
                null,
                null,
                null,
                Optional.ofNullable(getTrailer())
                        .map(VideoMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(getVideo())
                        .map(VideoMediaJpaEntity::toDomain)
                        .orElse(null),
                null,
                null,
                null
        );
    }

    public UUID getId() {
        return id;
    }

    public VideoJpaEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoJpaEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public VideoJpaEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public VideoJpaEntity setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
        return this;
    }

    public boolean isOpened() {
        return opened;
    }

    public VideoJpaEntity setOpened(boolean opened) {
        this.opened = opened;
        return this;
    }

    public boolean isPublished() {
        return published;
    }

    public VideoJpaEntity setPublished(boolean published) {
        this.published = published;
        return this;
    }

    public Rating getRating() {
        return rating;
    }

    public VideoJpaEntity setRating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public VideoJpaEntity setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public VideoJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public VideoJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public VideoMediaJpaEntity getVideo() {
        return video;
    }

    public VideoJpaEntity setVideo(VideoMediaJpaEntity video) {
        this.video = video;
        return this;
    }

    public VideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public VideoJpaEntity setTrailer(VideoMediaJpaEntity trailer) {
        this.trailer = trailer;
        return this;
    }

}