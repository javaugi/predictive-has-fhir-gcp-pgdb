/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

import com.sisllc.instaiml.model.aiml.MedicalDocument;
import com.sisllc.instaiml.repository.aiml.MedicalDocumentRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import java.util.ArrayList;
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
public class MedicalDocumentRagService {
    private final MedicalDocumentRepository documentRepository;
    private final OpenAiService openAiService;

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
            .map(req -> openAiService.createEmbeddings(req))
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

    private Mono<String> generateAnswer(String question, List<MedicalDocument> documents) {
        return Mono.fromCallable(() -> {
            // Build context from retrieved documents
            String context = documents.stream()
                .map(doc -> String.format("Title: %s\nSpecialty: %s\nContent: %s",
                doc.getTitle(), doc.getSpecialty(),
                doc.getTextContent().substring(0, Math.min(2000, doc.getTextContent().length()))))
                .collect(Collectors.joining("\n\n"));

            // Create chat completion with medical-focused prompt
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "You are a helpful medical AI assistant. Use the provided medical context to answer questions. "
                + "If you don't know the answer, say you don't know. Be precise and cite sources when possible."));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(),
                "Context:\n" + context + "\n\nQuestion: " + question));

            ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                .model("gpt-4")
                .messages(messages)
                .temperature(0.3)
                .build();

            return openAiService.createChatCompletion(chatRequest)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
