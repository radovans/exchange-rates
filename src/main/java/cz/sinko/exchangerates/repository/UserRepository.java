package cz.sinko.exchangerates.repository;

import cz.sinko.exchangerates.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity.
 *
 * @author Radovan Šinko
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
