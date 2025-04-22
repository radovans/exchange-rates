package cz.sinko.exchangerates.api.controller;

import cz.sinko.exchangerates.service.ForexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PoetryController
 *
 * @author Radovan Šinko
 */
@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;

    @GetMapping("/predict")
    public ResponseEntity<String> predict() {
        return ResponseEntity.ok(forexService.predict());
    }
}
