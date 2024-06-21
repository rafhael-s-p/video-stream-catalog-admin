package com.studies.catalog.admin.infrastructure.video.persistence;

import com.studies.catalog.admin.domain.video.MediaStatus;
import com.studies.catalog.admin.domain.video.VideoMedia;

import javax.persistence.*;

@Entity(name = "VideoMedia")
@Table(name = "video_medias")
public class VideoMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Column(name = "media_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public VideoMediaJpaEntity() {
    }

    private VideoMediaJpaEntity(
            final String id,
            final String checksum,
            final String name,
            final String filePath,
            final String encodedPath,
            final MediaStatus status
    ) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static VideoMediaJpaEntity from(final VideoMedia media) {
        return new VideoMediaJpaEntity(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status()
        );
    }

    public VideoMedia toDomain() {
        return VideoMedia.with(
                getId(),
                getChecksum(),
                getName(),
                getFilePath(),
                getEncodedPath(),
                getStatus()
        );
    }

    public String getId() {
        return id;
    }

    public VideoMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public VideoMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getName() {
        return name;
    }

    public VideoMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public VideoMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public VideoMediaJpaEntity setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
        return this;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public VideoMediaJpaEntity setStatus(MediaStatus status) {
        this.status = status;
        return this;
    }

}