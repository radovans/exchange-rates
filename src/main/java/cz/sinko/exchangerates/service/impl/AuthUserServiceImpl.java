package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for loading user details.
 *
 * @author Radovan Šinko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Finding username '{}'", username);
        final UserDetails userDetails = userRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("User with username '{}' not found", username);
            return new UsernameNotFoundException("User with username '" + username + "' not found");
        });
        log.info("User with username '{}' found", username);
        return userDetails;
    }
}
