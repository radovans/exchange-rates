package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.api.dto.response.user.UserResponse;
import cz.sinko.exchangerates.api.mapper.UserApiMapper;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import cz.sinko.exchangerates.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
