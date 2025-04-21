package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.service.ForexService;
import io.getunleash.Unleash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ForexServiceImpl
 *
 * @author Radovan Šinko
 */
@Service
@Slf4j
public class ForexServiceImpl implements ForexService {

    private final ChatClient chatClient;

    private final Unleash unleash;

    /**
     * ForexServiceImpl constructor
     *
     * @param chatClientBuilder chat client builder
     * @param unleash           unleash
     */
    public ForexServiceImpl(final ChatClient.Builder chatClientBuilder, final Unleash unleash) {
        this.chatClient = chatClientBuilder.build();
        this.unleash = unleash;
    }

    @Override
    public String predict() {
        if (!unleash.isEnabled("openAiClient")) {
            log.info("openAiClient is disabled");
            throw new RuntimeException("openAiClient is disabled");
        }

        final List<Double> rates = List.of(23.5, 23.6, 23.7, 23.8, 23.9, 24.0, 24.1);

        final String input = "Here are the last 7 days of EUR/CZK rates: " + rates +
                ". What is your prediction for the next day?";
        return chatClient.prompt().user(input).call().content();
    }
}
