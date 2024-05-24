package com.studies.catalog.admin.application.castmember.delete;

import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DeleteCastMemberUseCaseImpl extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.castMemberGateway.deleteById(CastMemberID.from(anIn));
    }

}