package com.studies.catalog.admin.infrastructure.api;

import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberApiResponse;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import com.studies.catalog.admin.infrastructure.castmember.models.UpdateCastMemberApiRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "cast_members")
@Tag(name = "Cast Members")
public interface CastMemberAPI {

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member retrieved"),
            @ApiResponse(responseCode = "404", description = "Cast member was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred"),
    })
    CastMemberApiResponse getById(@PathVariable String id);

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new cast member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error has occurred"),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred"),
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberApiRequest input);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member updated"),
            @ApiResponse(responseCode = "404", description = "Cast member was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error has occurred"),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred"),
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCastMemberApiRequest aBody);
}