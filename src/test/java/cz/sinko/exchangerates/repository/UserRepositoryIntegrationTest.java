package cz.sinko.exchangerates.repository;

import cz.sinko.exchangerates.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for {@link UserRepository}
 *
 * @author Radovan Šinko
 */
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        final User user = User.builder().username("alice").name("Alice").password("Password123").build();
        user.setUsername(user.getUsername());
        entityManager.persist(user);
        entityManager.flush();

        final User foundUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }
}
