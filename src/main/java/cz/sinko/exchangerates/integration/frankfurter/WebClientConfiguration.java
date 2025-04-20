package cz.sinko.exchangerates.integration.frankfurter;

import cz.sinko.exchangerates.configuration.Constants;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.Request;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.marker.Markers.append;

/**
 * WebClient configuration.
 *
 * @author Radovan Šinko
 */
@Configuration
@EnableConfigurationProperties(FrankfurterClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class WebClientConfiguration {

    private final FrankfurterClientProperties frankfurterClientProperties;

    /**
     * WebClient bean for Frankfurter API.
     *
     * @return WebClient
     */
    @Bean("frankfurterWebClient")
    public WebClient jettyHttpClient() {
        Assert.notNull(frankfurterClientProperties, "frankfurterClientProperties must not be null");
        Assert.notNull(frankfurterClientProperties.getUrl(), "integration.frankfurter-client.url must not be null");

        final HttpClient httpClient = new HttpClient() {
            @Override
            public Request newRequest(final URI uri) {
                final String traceId = MDC.get(Constants.TRACE_ID);
                final String userId = MDC.get(Constants.USER_ID);
                final Request request = super.newRequest(uri);
                return enhance(request, traceId, userId);
            }
        };

        return WebClient.builder()
                .clientConnector(new JettyClientHttpConnector(httpClient))
                .baseUrl(frankfurterClientProperties.getUrl())
                .build();
    }

    /**
     * Method that adds logging to the incoming request
     *
     * @param inboundRequest the request to enhance
     * @param traceId        the trace ID
     * @param userId         the user ID
     * @return the request
     */
    private Request enhance(final Request inboundRequest, final String traceId, final String userId) {
        final ByteArrayOutputStream requestBodyBuffer = new ByteArrayOutputStream();
        final ByteArrayOutputStream responseBodyBuffer = new ByteArrayOutputStream();
        final Map<String, String> requestHeaders = new HashMap<>();
        final Map<String, String> responseHeaders = new HashMap<>();

        // Request Logging
        inboundRequest.onRequestHeaders(request -> {
            request.getHeaders().forEach(field -> requestHeaders.put(field.getName(), field.getValue()));
        });

        inboundRequest.onRequestContent((request, content) -> {
            final ByteBuffer duplicate = content.duplicate();
            final byte[] bytes = new byte[duplicate.remaining()];
            duplicate.get(bytes);
            requestBodyBuffer.write(bytes, 0, bytes.length);
        });

        inboundRequest.onRequestSuccess(request -> {
            MDC.put(Constants.TRACE_ID, traceId);
            MDC.put(Constants.USER_ID, userId);

            final WebClientRequest req = WebClientRequest.builder()
                    .url(request.getURI().toString())
                    .method(request.getMethod())
                    .headers(requestHeaders)
                    .body(requestBodyBuffer.toString(StandardCharsets.UTF_8))
                    .build();
            if (log.isInfoEnabled()) {
                log.info(append("request", req), "Outgoing WebClient request '{}'", req);
            }
        });

        // Response Logging
        inboundRequest.onResponseHeaders(response -> {
            response.getHeaders().forEach(field -> responseHeaders.put(field.getName(), field.getValue()));
        });

        inboundRequest.onResponseContent((response, content) -> {
            final ByteBuffer duplicate = content.duplicate();
            final byte[] bytes = new byte[duplicate.remaining()];
            duplicate.get(bytes);
            responseBodyBuffer.write(bytes, 0, bytes.length);
        });

        inboundRequest.onResponseSuccess(response -> {
            MDC.put(Constants.TRACE_ID, traceId);
            MDC.put(Constants.USER_ID, userId);

            final Request request = response.getRequest();
            final WebClientResponse resp = WebClientResponse.builder()
                    .url(request.getURI().toString())
                    .method(request.getMethod())
                    .statusCode(response.getStatus())
                    .headers(responseHeaders)
                    .body(responseBodyBuffer.toString(StandardCharsets.UTF_8))
                    .build();
            if (log.isInfoEnabled()) {
                log.info(append("response", resp), "Incoming WebClient response '{}", resp);
            }
        });

        return inboundRequest;
    }

    @Data
    @Builder
    private final static class WebClientRequest {
        private String url;
        private String method;
        private Map<String, String> headers;
        private Map<String, Object> attributes;
        private String body;
    }

    @Data
    @Builder
    private final static class WebClientResponse {
        private String url;
        private String method;
        private int statusCode;
        private Map<String, String> headers;
        private String body;
    }
}
