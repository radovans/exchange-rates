package cz.sinko.exchangerates.service;

/**
 * ForexService
 *
 * @author Radovan Šinko
 */
public interface ForexService {

    /**
     * Predict exchange rate
     *
     * @return prediction
     */
    String predict();
}
