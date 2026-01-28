package com.deepseek.model;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelFactory {

//    /**
//     * 可以自定义消息记忆的容量，默认20条（10轮对话）
//     *
//     * @param chatMemoryRepository 默认使用基于内存的仓库
//     * @return 自定义ChatMemory
//     */
//    @Bean("chatMemory")
//    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
//        return MessageWindowChatMemory.builder()
//                .chatMemoryRepository(chatMemoryRepository)
//                .maxMessages(1).build();
//    }

    @Bean("deepSeekChatClient")
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder().temperature(1.0).build()) // 精确度
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build(), // 上下文记忆
                        new SafeGuardAdvisor(List.of("死")))  // 敏感词
                .build();
    }
}
