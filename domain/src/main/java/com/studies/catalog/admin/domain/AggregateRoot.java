package com.studies.catalog.admin.domain;

public abstract class AggregateRoot<GenericId extends Identifier> extends Entity<GenericId> {

    protected AggregateRoot(final GenericId id) {
        super(id);
    }

}