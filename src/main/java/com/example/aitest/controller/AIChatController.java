package com.example.aitest.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/ai")
public class AIChatController {
    private final ChatClient chatClient;

    public AIChatController(ChatClient.Builder builder) {
        chatClient = builder
                .defaultSystem("""
                        你是一个天气预报员。
                        获取当地天气情况前，从用户处获取用户要查询的地点，询问前，请从历史消息中获取此信息。
                        请讲中文。
                        今天的日期是{current_date}.
                        """)
                .defaultAdvisors(
                        // 对话记忆
                        new PromptChatMemoryAdvisor(new InMemoryChatMemory()))
                // Function call
                .defaultFunctions("getWeather")
                .build();
    }

    @GetMapping("/chat")
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                // 参数传入
                .system(promptSystemSpec -> promptSystemSpec.param("current_date", LocalDate.now().toString()))
                // 对话记忆的容量
                .advisors(advisorSpec -> advisorSpec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY,100))
                .call()
                .content();
    }
}
