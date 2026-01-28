package com.deepseek.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ai")
public class AiController {

    @Autowired
    private ChatClient deepSeekChatClient;

    @Value("classpath:/prompts/weather.st")
    private Resource weatherResource;

    @GetMapping(value = "/weather")
    public String weather(String message) {
        return deepSeekChatClient.prompt()
                .system(weatherResource)
//                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "111")) // 设置用户对话记忆的唯一标识
                .user(message)
                .call().content();
    }
}
