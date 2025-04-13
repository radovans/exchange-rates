package cz.sinko.exchangerates.repository;

import cz.sinko.exchangerates.repository.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Role entity.
 *
 * @author Radovan Šinko
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its authority.
     *
     * @param role the authority of the role
     * @return an Optional containing the Role if found, or empty if not found
     */
    Optional<Role> findByAuthority(String role);
}
