package com.studies.catalog.admin.application.castmember.delete;

import com.studies.catalog.admin.application.UnitUseCase;

public abstract sealed class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DeleteCastMemberUseCaseImpl {
}