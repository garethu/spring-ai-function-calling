package com.yootiful.functioncalling.service;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AdviceService {
    private final OllamaChatModel chatModel;

    public AdviceService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String answer(String text) {
        SystemMessage systemMessage = new SystemMessage("""
                You are a helpful AI assistant that provides insight into cryptocurrencies based
                on realtime quotations. Answer the user question based on your knowledge about the
                market and the available tools for retrieving quotation info as needed. If the user's
                question has nothing to do with cryptocurrencies or the associated markets,
                just say you don't know the answer.
                """);
        UserMessage userMessage = new UserMessage(text);

        return getChatResponse(List.of(systemMessage, userMessage))
                .getResult().getOutput().getContent();
    }

    private ChatResponse getChatResponse(List<Message> messages) {
        Instant start = Instant.now();

        var response = chatModel.call(
                new Prompt(
                        messages,
                        OllamaOptions.builder()
                                .withModel("llama3.1")
                                .withFunction("CurrentQuotation")
                                .build()
                )
        );

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        // Display time taken in milliseconds, seconds, or other units as needed
        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
        System.out.println("Time taken: " + timeElapsed.toSeconds() + " seconds");

        return response;
    }
}
