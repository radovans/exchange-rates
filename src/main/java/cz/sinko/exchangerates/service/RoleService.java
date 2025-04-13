package cz.sinko.exchangerates.service;

import cz.sinko.exchangerates.repository.entity.Role;

/**
 * Service for User operations.
 *
 * @author Radovan Šinko
 */
public interface RoleService {

    /**
     * Find Role by authority.
     *
     * @param authority Role authority
     * @return Role
     */
    Role findByAuthority(String authority);
}
