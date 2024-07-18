package com.studies.catalog.admin.infrastructure.services;

public interface EventService {
    void send(Object event);
}