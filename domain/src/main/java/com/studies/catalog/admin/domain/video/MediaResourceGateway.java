package com.studies.catalog.admin.domain.video;

public interface MediaResourceGateway {

    VideoMedia storeVideo(VideoID anId, Resource aResource);

    ImageMedia storeImage(VideoID anId, Resource aResource);

    void clearResources(VideoID anId);

}