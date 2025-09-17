/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

import com.sisllc.instaiml.config.OllamaProperties;
import com.sisllc.instaiml.model.aiml.MedicalDocument;
import com.sisllc.instaiml.repository.aiml.MedicalDocumentRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.embedding.EmbeddingRequest;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaRagMediDocService {
    private final MedicalDocumentRepository documentRepository;
    private final OllamaService ollamaService;
    private final OllamaProperties properties;

    public Mono<String> answerQuestion(String question) {
        List<ChatMessage> messages = List.of(
            new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant"),
            new ChatMessage(ChatMessageRole.USER.value(), question)
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .messages(messages)
            .model(properties.getModel())
            .temperature(0.7)
            .build();

        return ollamaService.createChatCompletion(request)
            .map(result -> result.getChoices().get(0).getMessage().getContent());
    }

    public Mono<String> answerMedicalQuestion(String question) {
        return getRelevantDocuments(question)
            .flatMap(docs -> generateAnswer(question, docs));
    }

    private Mono<List<MedicalDocument>> getRelevantDocuments(String question) {
        EmbeddingRequest request = EmbeddingRequest.builder()
            .model("text-embedding-ada-002")
            .input(List.of(question))
            .build();

        return Mono.just(request)
            .publishOn(Schedulers.boundedElastic())
            .flatMap(req -> ollamaService.createEmbeddings(req))
            .map(result -> {
                double[] doubles = result.getData().get(0).getEmbedding().stream()
                    .mapToDouble(Double::doubleValue)
                    .toArray();

                float[] embedding = new float[doubles.length];
                for (int i = 0; i < doubles.length; i++) {
                    embedding[i] = (float) doubles[i];
                }
                return embedding;
            })
            .flatMap(embedding -> documentRepository.findSimilarDocuments(embedding, 3).collectList());
    }

    public Mono<List<Double>> getEmbedding(String question) {
        EmbeddingRequest request = EmbeddingRequest.builder()
            .input(List.of(question))
            .model(properties.getEmbeddingModel())
            .build();

        return ollamaService.createEmbeddings(request)
            .map(result -> result.getData().get(0).getEmbedding());
    }

    public Mono<String> generateAnswer(String question, List<MedicalDocument> documents) {
        // Build the context reactively
        return Mono.fromCallable(() -> buildContext(documents))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(context -> {
                // Prepare the chat messages
                List<ChatMessage> messages = List.of(
                    new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "You are a helpful medical AI assistant. Use the provided medical context to answer questions. "
                        + "If you don't know the answer, say you don't know. Be precise and cite sources when possible."),
                    new ChatMessage(ChatMessageRole.USER.value(),
                        "Context:\n" + context + "\n\nQuestion: " + question)
                );

                // Build the chat request
                ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                    .model("llama2") // or "medllama2" if using a medical fine-tuned model
                    .messages(messages)
                    .temperature(0.3)
                    .build();

                // Call Ollama service reactively
                return ollamaService.createChatCompletion(chatRequest)
                    .map(result -> {
                        if (result.getChoices() == null || result.getChoices().isEmpty()) {
                            throw new RuntimeException("No response generated");
                        }
                        return result.getChoices().get(0).getMessage().getContent();
                    });
            })
            .timeout(Duration.ofSeconds(30)) // Add timeout
            .onErrorResume(e -> {
                log.error("Error generating answer", e);
                return Mono.just("I encountered an error processing your medical question. Please try again later.");
            });
    }

    private String buildContext(List<MedicalDocument> documents) {
        return documents.stream()
            .map(doc -> String.format("Title: %s\nSpecialty: %s\nContent: %s",
            doc.getTitle(),
            doc.getSpecialty(),
            doc.getTextContent().substring(0, Math.min(2000, doc.getTextContent().length()))))
            .collect(Collectors.joining("\n\n"));
    }
}
