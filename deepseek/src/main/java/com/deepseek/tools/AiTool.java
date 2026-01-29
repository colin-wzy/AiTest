package com.deepseek.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class AiTool {

    @Tool(description = "退订")
    public String cancel(@ToolParam(description = "姓名") String name,
                         @ToolParam(description = "订单号，可以是纯数字") String orderNumber) {
        // 可以调用业务代码
        return "退订成功";
    }
}
