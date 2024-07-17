package com.studies.catalog.admin.domain.events;

@FunctionalInterface
public interface DomainEventPublisher<T extends DomainEvent> {
    void publishEvent(T event);
}