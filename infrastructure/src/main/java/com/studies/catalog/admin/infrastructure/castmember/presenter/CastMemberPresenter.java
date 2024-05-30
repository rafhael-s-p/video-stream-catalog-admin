package com.studies.catalog.admin.infrastructure.castmember.presenter;

import com.studies.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberApiResponse;

public interface CastMemberPresenter {

    static CastMemberApiResponse present(final CastMemberOutput aMember) {
        return new CastMemberApiResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

}