package com.spring.webflux.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * WebSocket 支持客户端与服务器端的双向通讯。当客户端与服务器端之间的交互方式比较复杂时，可以使用 WebSocket。WebSocket 在主流的浏览器上都得到了支持。WebFlux 也
 * 对创建 WebSocket 服务器端提供了支持。在服务器端，我们需要实现接口 org.springframework.web.reactive.socket.WebSocketHandler 来处理 WebSocket 通讯。接口
 * WebSocketHandler 的方法 handle 的参数是接口 WebSocketSession 的对象，可以用来获取客户端信息、接送消息和发送消息。
 * <p>
 * 代码清单 6 中的 EchoHandler 对于每个接收的消息，会发送一个添加了"ECHO -> "前缀的响应消息。WebSocketSession 的 receive 方法的返回值是一个 Flux<WebSocketMessage>
 * 对象，表示的是接收到的消息流。而 send 方法的参数是一个 Publisher<WebSocketMessage>对象，表示要发送的消息流。在 handle 方法，使用 map 操作对 receive 方法得到的
 * Flux<WebSocketMessage>中包含的消息继续处理，然后直接由 send 方法来发送。
 *
 * @author xuweizhi
 * @since 2019-07-04
 */
@Component
public class EchoHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        return session.send(
                session.receive()
                        .map(msg -> session.textMessage("ECHO -> " + msg.getPayloadAsText())));
    }

}
