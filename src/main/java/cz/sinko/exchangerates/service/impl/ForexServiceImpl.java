package cz.sinko.exchangerates.service.impl;

import cz.sinko.exchangerates.service.ForexService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PoetryServiceImpl
 *
 * @author Radovan Šinko
 */
@Service
public class ForexServiceImpl implements ForexService {

    private final ChatClient chatClient;

    public ForexServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String predict() {
        List<Double> rates = List.of(23.5, 23.6, 23.7, 23.8, 23.9, 24.0, 24.1);

        String input = "Here are the last 7 days of EUR/CZK rates: " + rates +
                ". What is your prediction for the next day?";
        return chatClient.prompt().user(input).call().content();
    }
}