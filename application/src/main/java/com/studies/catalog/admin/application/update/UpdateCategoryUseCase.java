package com.studies.catalog.admin.application.update;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {}