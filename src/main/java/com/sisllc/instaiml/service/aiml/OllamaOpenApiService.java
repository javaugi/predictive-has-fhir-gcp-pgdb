/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaOpenApiService extends OpenAiService {

    @Autowired
    OllamaService ollamaService;

    public OllamaOpenApiService(String token) {
        super(token);
    }

    /*
    public OllamaOpenApiService(String token, Duration timeout) {
        super(token, timeout);
    }

    public OllamaOpenApiService(OpenAiApi api) {
        super(api);
    }

    public OllamaOpenApiService(OpenAiApi api, ExecutorService executorService) {
        super(api, executorService);
    }
    // */

    @Override
    public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
        return ollamaService.createEmbeddings(request).block(Duration.ofSeconds(10));
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return ollamaService.createChatCompletion(request).block(Duration.ofSeconds(10));
    }
}
