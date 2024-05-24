package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;

public final class CreateCastMemberUseCaseImpl extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public CreateCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberInput anInput) {
        final var aName = anInput.name();
        final var aType = anInput.type();

        final var notification = Notification.create();

        final var aMember = notification.validate(() -> CastMember.newMember(aName, aType));

        if (notification.hasError()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }
}
