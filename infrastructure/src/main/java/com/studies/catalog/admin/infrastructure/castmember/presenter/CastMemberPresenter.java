package com.studies.catalog.admin.infrastructure.castmember.presenter;

import com.studies.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
import com.studies.catalog.admin.application.castmember.retrieve.list.CastMemberListOutput;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberApiResponse;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberListApiResponse;

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

    static CastMemberListApiResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListApiResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }

}