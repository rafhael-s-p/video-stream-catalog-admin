package com.studies.catalog.admin.domain;

import com.studies.catalog.admin.domain.events.DomainEvent;

import java.util.List;

public abstract class AggregateRoot<GenericId extends Identifier> extends Entity<GenericId> {

    protected AggregateRoot(final GenericId id) {
        super(id);
    }

    protected AggregateRoot(final GenericId id, final List<DomainEvent> events) {
        super(id, events);
    }

}