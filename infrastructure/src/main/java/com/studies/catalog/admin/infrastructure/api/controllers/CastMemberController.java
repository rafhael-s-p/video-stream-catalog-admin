package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.castmember.create.CreateCastMemberInput;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.studies.catalog.admin.infrastructure.api.CastMemberAPI;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberApiResponse;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import com.studies.catalog.admin.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    public CastMemberController(final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
                                final CreateCastMemberUseCase createCastMemberUseCase) {
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public CastMemberApiResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberApiRequest input) {
        final var anInput =
                CreateCastMemberInput.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

}