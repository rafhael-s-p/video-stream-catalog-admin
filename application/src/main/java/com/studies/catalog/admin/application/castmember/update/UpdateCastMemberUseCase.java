package com.studies.catalog.admin.application.castmember.update;

import com.studies.catalog.admin.application.UseCase;

public abstract sealed class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberInput, UpdateCastMemberOutput>
        permits UpdateCastMemberUseCaseImpl {
}