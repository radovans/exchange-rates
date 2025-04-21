package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.repository.entity.User;
import cz.sinko.exchangerates.service.ForexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ForexController
 *
 * @author Radovan Šinko
 */
@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;

    /**
     * Predict exchange rate
     *
     * @param loggedUser logged user
     * @return prediction
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/predict")
    public ResponseEntity<String> predict(@AuthenticationPrincipal final User loggedUser) {
        return ResponseEntity.ok(forexService.predict());
    }
}
