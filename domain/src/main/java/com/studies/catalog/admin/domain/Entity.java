package com.studies.catalog.admin.domain;

import com.studies.catalog.admin.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entity<GenericId extends Identifier> {

    protected final GenericId id;

    protected Entity(final GenericId id) {
        Objects.requireNonNull(id, "'id' should not be null!");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public GenericId getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;

        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
