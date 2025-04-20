package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.api.ApiError;
import cz.sinko.exchangerates.api.dto.request.user.UserCreateRequest;
import cz.sinko.exchangerates.api.dto.request.user.UserUpdateRequest;
import cz.sinko.exchangerates.api.dto.response.user.UserResponse;
import cz.sinko.exchangerates.api.mapper.UserApiMapper;
import cz.sinko.exchangerates.configuration.exception.AlreadyExistsException;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.facade.UserFacade;
import cz.sinko.exchangerates.repository.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.sinko.exchangerates.api.ApiUris.ROOT_URI_USERS;
import static net.logstash.logback.marker.Markers.append;

/**
 * Controller for User management.
 *
 * @author Radovan Šinko
 */
@RestController
@RequestMapping(path = ROOT_URI_USERS)
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "User", description = "User management")
public class UserController {

    private final UserFacade userFacade;
    private final UserApiMapper userApiMapper;

    /**
     * Get user by ID.
     *
     * @param id ID of the user to retrieve
     * @return the user
     * @throws ResourceNotFoundException if user not found
     */
    @Operation(summary = "Get user by id", description = "Retrieve a user by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable final long id) throws ResourceNotFoundException {

        log.info("Call getUser with id '{}'", id);
        return ResponseEntity.ok(userApiMapper.toResponse(userFacade.getUser(id)));
    }

    /**
     * Get all users.
     *
     * @return List of UserResponse
     */
    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            UserResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        log.info("Call getUsers");
        return ResponseEntity.ok().body(userApiMapper.toResponse(userFacade.getUsers()));
    }

    /**
     * Create new user.
     *
     * @param userCreateRequest the user create request
     * @return created user
     * @throws AlreadyExistsException if user already exists
     */
    @Operation(summary = "Create new user", description = "Create a new user account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "User data to create", required = true)
            @RequestBody @Valid final UserCreateRequest userCreateRequest) throws AlreadyExistsException {

        log.info(append("request", userCreateRequest), "Call createUser with request '{}'", userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userApiMapper.toResponse(userFacade.createUser(userApiMapper.fromRequest(userCreateRequest))));
    }

    /**
     * Delete user by ID.
     *
     * @param loggedUser the logged-in user
     * @param id         the id of the user to delete
     * @return void
     * @throws ResourceNotFoundException if user not found
     */
    @Operation(summary = "Delete user by id", description = "Delete a user by their unique ID.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal final User loggedUser,
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable final long id) throws ResourceNotFoundException {

        log.info("Call deleteUser with id '{}'", id);
        userFacade.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update user by ID.
     *
     * @param loggedUser        the logged-in user
     * @param id                the id of the user to update
     * @param userUpdateRequest the user update request
     * @return updated user
     * @throws ResourceNotFoundException if user not found
     */
    @Operation(summary = "Update user by id", description = "Update an existing user.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal final User loggedUser,
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable final long id,
            @Parameter(description = "User data to update", required = true)
            @RequestBody @Valid final UserUpdateRequest userUpdateRequest) throws ResourceNotFoundException {

        log.info(append("request", userUpdateRequest),
                "Call updateUser with id '{}' and request '{}'", id, userUpdateRequest);
        return ResponseEntity.ok().body(
                userApiMapper.toResponse(
                        userFacade.updateUser(loggedUser, id, userApiMapper.fromRequest(userUpdateRequest))));
    }
}
