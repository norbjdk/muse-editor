package com.muse.server.config;

import com.muse.server.websocket.ScoreWebSocketHandler;
import com.muse.server.websocket.WebSocketAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ScoreWebSocketHandler scoreWebSocketHandler;

    @Autowired
    private WebSocketAuthInterceptor authInterceptor;

    public WebSocketConfig(ScoreWebSocketHandler scoreWebSocketHandler,
                           WebSocketAuthInterceptor authInterceptor) {
        this.scoreWebSocketHandler = scoreWebSocketHandler;
        this.authInterceptor       = authInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(scoreWebSocketHandler, "/ws/collab")
                .addInterceptors(authInterceptor)
                .setAllowedOrigins("*");
    }
}
