package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.castmember.create.CreateCastMemberInput;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.studies.catalog.admin.infrastructure.api.CastMemberAPI;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberApiRequest input) {
        final var anInput =
                CreateCastMemberInput.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

}