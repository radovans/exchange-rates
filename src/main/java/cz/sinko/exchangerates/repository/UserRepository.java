package cz.sinko.exchangerates.repository;

import cz.sinko.exchangerates.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity.
 *
 * @author Radovan Šinko
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by its username.
     *
     * @param username the username of the user
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
}
