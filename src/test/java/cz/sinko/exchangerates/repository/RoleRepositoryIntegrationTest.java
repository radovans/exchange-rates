package cz.sinko.exchangerates.repository;

import cz.sinko.exchangerates.configuration.Constants;
import cz.sinko.exchangerates.repository.entity.Role;
import cz.sinko.exchangerates.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for {@link RoleRepository}
 *
 * @author Radovan Šinko
 */
@DataJpaTest
public class RoleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByAuthority() {
        final Role role = Role.builder().authority("ROLE_USER").build();
        final User user = User.builder()
                .username("alice")
                .name("Alice")
                .password("Password123")
                .authorities(Set.of(role))
                .build();
        entityManager.persist(role);
        entityManager.persist(user);
        entityManager.flush();

        final Optional<Role> foundRole = roleRepository.findByAuthority(Constants.USER_ROLE);

        assertNotNull(foundRole);
        assertEquals(role.getAuthority(), foundRole.get().getAuthority());
        assertEquals(role.getId(), foundRole.get().getId());
    }
}
