/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import com.sisllc.instaiml.dto.aiml.OllamaChatResponse;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingResult;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingUtils {
    public static List<Embedding> convertDoublesToEmbeddings(List<Double> doubleList) {
        Embedding embedding = new Embedding();
        embedding.setEmbedding(doubleList);
        return List.of(embedding);
    }

    public static List<ChatCompletionChoice> convertToChatChoices(OllamaChatResponse response) {
        ChatCompletionChoice choice = new ChatCompletionChoice();
        ChatMessage message = new ChatMessage(response.getMessage().getRole(), response.getMessage().getContent());
        choice.setMessage(message);

        return List.of(choice);
    }

    public static float[] convertToEmbeddings(EmbeddingResult result) {
        List<Double> list = new ArrayList<>();

        for (Embedding embedding : result.getData()) {
            list.addAll(embedding.getEmbedding());
        }

        float[] floatEmbedding = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            floatEmbedding[i] = list.get(i).floatValue();
        }
        return floatEmbedding;
    }
}
