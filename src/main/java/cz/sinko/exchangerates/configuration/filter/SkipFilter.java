package cz.sinko.exchangerates.configuration.filter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Skips operations for certain requests.
 *
 * @author Radovan Šinko
 */
public final class SkipFilter {

    /**
     * Do not instantiate.
     */
    private SkipFilter() {
        // Prevent instantiation
    }

    /**
     * Checks if the request action should be skipped.
     *
     * @param request the request
     * @return true if the request action should be skipped, false otherwise
     */
    public static boolean skip(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURI();
        return requestUrl != null && (requestUrl.contains("/actuator")
                || requestUrl.contains("/v3/api-docs")
                || requestUrl.contains("/swagger-ui.html")
                || requestUrl.contains("/swagger-resources")
                || requestUrl.contains("/csrf")
                || requestUrl.contains("/webjars/springfox-swagger-ui")
                || (requestUrl.endsWith("/") && requestUrl.length() == 1));
    }
}
