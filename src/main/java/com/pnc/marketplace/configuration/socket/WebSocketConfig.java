package com.pnc.marketplace.configuration.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final String[] origins = {"http://localhost:4200" };

    /**
     * The function registers a Stomp endpoint with the specified URL and allowed origin patterns.
     * 
     * @param registry The `registry` parameter is an instance of `StompEndpointRegistry`, which is
     * used to register STOMP (Simple Text Oriented Messaging Protocol) endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns(origins)
                .withSockJS();
    }

    /**
     * The function configures the message broker registry by setting application destination prefixes,
     * enabling simple brokers for chatroom and userChat, and setting user destination prefix.
     * 
     * @param registry The `registry` parameter is an instance of `MessageBrokerRegistry` which is used
     * to configure the message broker for WebSocket communication.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom", "/userChat");
        registry.setUserDestinationPrefix("/userChat");
    }
}
