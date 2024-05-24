package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.application.UseCase;

public abstract sealed class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberInput, CreateCastMemberOutput>
        permits CreateCastMemberUseCaseImpl {
}