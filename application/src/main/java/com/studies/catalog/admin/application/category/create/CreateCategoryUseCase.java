package com.studies.catalog.admin.application.category.create;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {}