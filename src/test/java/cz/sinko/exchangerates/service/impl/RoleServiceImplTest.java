package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.repository.RoleRepository;
import cz.sinko.exchangerates.repository.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static cz.sinko.exchangerates.configuration.Constants.ADMIN_ROLE;
import static cz.sinko.exchangerates.configuration.Constants.USER_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RoleServiceImpl}
 *
 * @author Radovan Šinko
 */
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByAuthorityReturnsRoleWhenAuthorityExists() {
        final Role role = createUserRole();
        when(roleRepository.findByAuthority(USER_ROLE)).thenReturn(Optional.of(role));

        final Role result = roleService.findByAuthority(USER_ROLE);

        assertNotNull(result);
        assertEquals(USER_ROLE, result.getAuthority());
        verify(roleRepository, times(1)).findByAuthority(USER_ROLE);
    }

    @Test
    void findByAuthorityThrowsExceptionWhenAuthorityDoesNotExist() {
        when(roleRepository.findByAuthority(ADMIN_ROLE)).thenReturn(Optional.empty());

        final String expectedMessage = "Authority 'ROLE_ADMIN' not present";
        final NoSuchElementException actualException = assertThrows(NoSuchElementException.class, () -> {
            roleService.findByAuthority(ADMIN_ROLE);
        });

        assertEquals(expectedMessage, actualException.getMessage());
        verify(roleRepository, times(1)).findByAuthority(ADMIN_ROLE);
    }

    private static Role createUserRole() {
        return Role.builder().id(1L).authority(USER_ROLE).build();
    }
}
