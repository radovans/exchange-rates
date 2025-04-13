package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.api.dto.request.user.UserCreateRequest;
import cz.sinko.exchangerates.api.dto.request.user.UserUpdateRequest;
import cz.sinko.exchangerates.api.dto.response.user.UserResponse;
import cz.sinko.exchangerates.api.mapper.UserApiMapper;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.sinko.exchangerates.api.ApiUris.ROOT_URI_USERS;

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
public class UserController {

    private final UserFacade userFacade;

    private final UserApiMapper userApiMapper;

    /**
     * Get user by id.
     *
     * @param id the id of the user
     * @return the user
     * @throws ResourceNotFoundException if user not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final long id) throws ResourceNotFoundException {
        log.info("Call getUser with id '{}'", id);
        return ResponseEntity.ok(userApiMapper.toResponse(userFacade.getUser(id)));
    }

    /**
     * Get all users.
     *
     * @return the list of users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        log.info("Call getUsers");
        return ResponseEntity.ok().body(userApiMapper.toResponse(userFacade.getUsers()));
    }

    /**
     * Create new user.
     *
     * @param userCreateRequest the user create request
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid final UserCreateRequest userCreateRequest) {
        log.info("Call createUser with request '{}'", userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userApiMapper.toResponse(userFacade.createUser(userApiMapper.fromRequest(userCreateRequest))));
    }

    /**
     * Delete user by id.
     *
     * @param id the id of the user to delete
     * @return void
     * @throws ResourceNotFoundException if user not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final long id) throws ResourceNotFoundException {
        log.info("Call deleteUser with id '{}'", id);
        userFacade.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update user by id.
     *
     * @param id                the id of the user to update
     * @param userUpdateRequest the user update request
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable final long id,
            @RequestBody @Valid final UserUpdateRequest userUpdateRequest) throws ResourceNotFoundException {

        log.info("Call updateUser with id '{}' and request '{}'", id, userUpdateRequest);
        return ResponseEntity.ok().body(userApiMapper.toResponse(
                userFacade.updateUser(id, userApiMapper.fromRequest(userUpdateRequest))));
    }
}
