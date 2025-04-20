package cz.sinko.exchangerates.configuration.filter;

import cz.sinko.exchangerates.configuration.Constants;
import cz.sinko.exchangerates.repository.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for setting user login to MDC.
 *
 * @author Radovan Šinko
 */
@Component
public class UsernameFilter extends OncePerRequestFilter {

    /**
     * Filter for setting user login to MDC.
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request,
                                    final @NonNull HttpServletResponse response,
                                    final @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (!SkipFilter.skip(request)) {
            final String userLogin = getUserLogin();

            try {
                MDC.put(Constants.USER_ID, userLogin);
                filterChain.doFilter(request, response);
            } finally {
                MDC.clear();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Get the current authenticated user's login from Spring Security.
     *
     * @return the user's login or null if not authenticated
     */
    private String getUserLogin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getUsername();
            }
        }
        return null; // Return null if no user is authenticated
    }
}
