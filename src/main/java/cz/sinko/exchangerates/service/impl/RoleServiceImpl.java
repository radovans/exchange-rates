package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.repository.RoleRepository;
import cz.sinko.exchangerates.repository.entity.Role;
import cz.sinko.exchangerates.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Implementation of {@link RoleService}
 *
 * @author Radovan Šinko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByAuthority(final String authority) {
        return roleRepository.findByAuthority(authority).orElseThrow(() -> new NoSuchElementException("Authority not " +
                "present"));
    }
}
