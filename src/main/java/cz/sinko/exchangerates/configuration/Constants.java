package cz.sinko.exchangerates.configuration;

/**
 * Class containing constants used in the application.
 *
 * @author Radovan Šinko
 */
public final class Constants {

    // Spring Security authorities
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    // MDC keys
    public static final String TRACE_ID = "traceId";
    public static final String USER_ID = "userId";

    /**
     * Do not instantiate.
     */
    private Constants() {
        // Prevent instantiation
    }
}
