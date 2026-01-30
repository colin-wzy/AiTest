package com.deepseek.controller;

import com.deepseek.function.TimeService;
import com.deepseek.record.Person;
import com.deepseek.tools.AiTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/ai")
public class AiController {

    @Autowired
    private ChatClient deepSeekChatClient;

    @Value("classpath:/prompts/weather.st")
    private Resource weatherResource;

    @GetMapping(value = "/stream")
    public Flux<String> stream(String message) {
        return deepSeekChatClient.prompt()
                .user(message)
                .stream().content();
    }

    @GetMapping(value = "/weather")
    public String weather(String message) {
        return deepSeekChatClient.prompt()
                .system(weatherResource)
//                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "111")) // 设置用户对话记忆的唯一标识
                .user(message)
                .call().content();
    }

    /**
     * 结构化输出
     *
     * @param message 输入
     * @return 输出对象
     */
    @GetMapping(value = "/person")
    public Person person(String message) {
        return deepSeekChatClient.prompt()
                .system("""
                        从文本中提取名字
                        """)
                .user(message)
                .call().entity(Person.class);
    }

    /**
     * tool应用（目前不生效，原因未知）
     *
     * @param message 输入
     * @return 输出对象
     */
    @GetMapping(value = "/cancel")
    public String cancel(String message) {
        return deepSeekChatClient.prompt()
                .tools(new AiTool())
                .user(message)
                .call().content();
    }

    /**
     * toolCallbacks应用（目前不生效，原因未知）
     *
     * @param message 输入
     * @return 输出对象
     */
    @GetMapping(value = "/time")
    public String time(String message) {
        FunctionToolCallback<Void, String> callback =
                FunctionToolCallback.builder("currentTime", new TimeService())
                        .description("用户询问当前时间相关问题时，请调用该工具，不要直接回答")
                        .inputType(Void.class)
                        .build();

        return deepSeekChatClient.prompt()
                .toolCallbacks(callback)
                .user(message)
                .call().content();
    }

    /**
     * MCP应用，已在{@link com.deepseek.model.ModelFactory#deepSeekChatClient}中统一配置
     *
     * @param message 输入
     * @return 输出对象
     */
    @GetMapping(value = "/mcp")
    public String mcp(String message) {
        return deepSeekChatClient.prompt()
                .user(message)
                .call().content();
    }
}
