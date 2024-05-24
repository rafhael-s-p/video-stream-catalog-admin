package com.studies.catalog.admin.application.castmember.update;

import com.studies.catalog.admin.domain.Identifier;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.exceptions.NotFoundException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public final class UpdateCastMemberUseCaseImpl extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public UpdateCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberInput anInput) {
        final var anId = CastMemberID.from(anInput.id());
        final var aName = anInput.name();
        final var aType = anInput.type();

        final var aMember = this.castMemberGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> aMember.update(aName, aType));

        if (notification.hasError()) {
            notify(anId, notification);
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(aMember));
    }

    private void notify(final Identifier anId, final Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);
    }

    private Supplier<NotFoundException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }

}