package com.spring.webflux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 在创建了 WebSocket 的处理器 EchoHandler 之后，下一步需要把它注册到 WebFlux 中。我们首先需要创建一个类 WebSocketHandlerAdapter 的
 * 对象，该对象负责把 WebSocketHandler 关联到 WebFlux 中。代码清单 7 中给出了相应的 Spring 配置。其中的 HandlerMapping 类型的 bean
 * 把 EchoHandler 映射到路径 /echo。
 *
 * @author xuweizhi
 */
@Configuration
public class WebSocketConfiguration {

    /**
     * 运行应用之后，可以使用工具来测试该 WebSocket 服务。打开工具页面 https://www.websocket.org/echo.html，
     * 然后连接到 ws://localhost:8080/echo，可以发送消息并查看服务器端返回的结果。
     */
    @Autowired
    @Bean
    public HandlerMapping webSocketMapping(final EchoHandler echoHandler) {

        final Map<String, WebSocketHandler> map = new HashMap<>(1);
        map.put("/echo", echoHandler);

        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
