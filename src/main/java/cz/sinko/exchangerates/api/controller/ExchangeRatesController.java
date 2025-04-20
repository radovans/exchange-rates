package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.api.ApiError;
import cz.sinko.exchangerates.api.dto.response.exchangerate.ExchangeRateResponse;
import cz.sinko.exchangerates.api.dto.response.exchangerate.SupportedCurrenciesResponse;
import cz.sinko.exchangerates.api.dto.response.exchangerate.TimeSeriesExchangeRatesResponse;
import cz.sinko.exchangerates.api.mapper.ExchangeRatesApiMapper;
import cz.sinko.exchangerates.facade.ExchangeFacade;
import cz.sinko.exchangerates.repository.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

import static cz.sinko.exchangerates.api.ApiUris.ROOT_URI_RATES;

/**
 * Controller for getting exchange rates.
 *
 * @author Radovan Šinko
 */
@RestController
@RequestMapping(path = ROOT_URI_RATES)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Exchange Rates", description = "Exchange rates")
public class ExchangeRatesController {

    private final ExchangeFacade exchangeFacade;

    private final ExchangeRatesApiMapper exchangeRatesApiMapper;

    /**
     * Get latest exchange rates.
     *
     * @param loggedUser logged user
     * @param base       base currency
     * @param symbols    target currencies
     * @param amount     amount to convert
     * @return latest exchange rates
     */
    @Operation(summary = "Get latest exchange rates", description = "Returns the latest exchange rates.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exchange rates retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ExchangeRateResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/latest")
    public ResponseEntity<ExchangeRateResponse> getLatestRates(
            @AuthenticationPrincipal final User loggedUser,
            @Parameter(description = "Base currency (e.g., EUR)") @RequestParam(required = false) final String base,
            @Parameter(description = "Comma-separated target currencies (e.g., USD,GBP)")
            @RequestParam(required = false) final String symbols,
            @Parameter(description = "Amount to convert") @RequestParam(required = false) final BigDecimal amount) {

        log.info("Call getLatestRates with base '{}', symbols '{}' and amount '{}'", base, symbols, amount);
        return ResponseEntity.ok(exchangeRatesApiMapper.toResponse(
                exchangeFacade.getLatestRates(base, symbols, amount)));
    }

    /**
     * Get exchange rates for a specific date.
     *
     * @param loggedUser logged user
     * @param date       date to get exchange rates for
     * @param base       base currency
     * @param symbols    target currencies
     * @param amount     amount to convert
     * @return exchange rates for the specified date
     */
    @Operation(summary = "Get exchange rates for a specific date", description = "Returns exchange rates for the " +
            "given date.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exchange rates retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ExchangeRateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date format or request parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/historical")
    public ResponseEntity<ExchangeRateResponse> getRatesForDate(
            @AuthenticationPrincipal final User loggedUser,
            @Parameter(description = "Date to get rates for (yyyy-MM-dd)", required = true)
            @RequestParam final LocalDate date,
            @Parameter(description = "Base currency (e.g., EUR)") @RequestParam(required = false) final String base,
            @Parameter(description = "Comma-separated target currencies (e.g., USD,GBP)")
            @RequestParam(required = false) final String symbols,
            @Parameter(description = "Amount to convert") @RequestParam(required = false) final BigDecimal amount) {

        log.info("Call getRatesForDate with date '{}', base '{}', symbols '{}' and amount '{}'",
                date, base, symbols, amount);
        return ResponseEntity.ok(exchangeRatesApiMapper.toResponse(
                exchangeFacade.getRatesForDate(date, base, symbols, amount)));
    }

    /**
     * Get exchange rates for a time series.
     *
     * @param loggedUser logged user
     * @param startDate  start date
     * @param endDate    end date
     * @param base       base currency
     * @param symbols    target currencies
     * @param amount     amount to convert
     * @return exchange rates for the specified time series
     */
    @Operation(summary = "Get exchange rates for a time series", description = "Returns exchange rates for a date " +
            "range.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time series exchange rates retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TimeSeriesExchangeRatesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range or request parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/timeseries")
    public ResponseEntity<TimeSeriesExchangeRatesResponse> getTimeSeriesRates(
            @AuthenticationPrincipal final User loggedUser,
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam final LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true) @RequestParam final LocalDate endDate,
            @Parameter(description = "Base currency (e.g., EUR)") @RequestParam(required = false) final String base,
            @Parameter(description = "Comma-separated target currencies (e.g., USD,GBP)")
            @RequestParam(required = false) final String symbols,
            @Parameter(description = "Amount to convert") @RequestParam(required = false) final BigDecimal amount) {

        log.info("Call getTimeSeriesRates with startDate '{}', endDate '{}', base '{}', symbols '{}' and amount '{}'",
                startDate, endDate, base, symbols, amount);
        return ResponseEntity.ok(exchangeRatesApiMapper.toResponse(
                exchangeFacade.getTimeSeriesRates(startDate, endDate, base, symbols, amount)));
    }

    /**
     * Get supported currencies.
     *
     * @param loggedUser logged user
     * @return supported currencies
     */
    @Operation(summary = "Get supported currencies", description = "Returns a list of supported currencies.",
            security = @SecurityRequirement(name = "BasicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Supported currencies retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            SupportedCurrenciesResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ApiError.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/currencies")
    public ResponseEntity<SupportedCurrenciesResponse> getSupportedCurrencies(
            @AuthenticationPrincipal final User loggedUser) {

        log.info("Call getSupportedCurrencies");
        return ResponseEntity.ok(exchangeRatesApiMapper.toResponse(exchangeFacade.getSupportedCurrencies()));
    }
}
