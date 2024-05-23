package com.studies.catalog.admin.domain.castmember;

import com.studies.catalog.admin.domain.AggregateRoot;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.utils.InstantUtils;
import com.studies.catalog.admin.domain.validation.ValidationHandler;
import com.studies.catalog.admin.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var anId = CastMemberID.unique();
        final var now = InstantUtils.now();

        return new CastMember(anId, aName, aType, now, now);
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError())
            throw new NotificationException("Failed to create an Aggregate CastMember", notification);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

}