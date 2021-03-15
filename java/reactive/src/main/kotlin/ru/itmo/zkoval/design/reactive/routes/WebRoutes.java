package ru.itmo.zkoval.design.reactive.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.itmo.zkoval.design.reactive.handler.ReactiveHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebRoutes {
    private final ReactiveHandler reactiveHandler;

    public WebRoutes(ReactiveHandler reactiveHandler) {
        this.reactiveHandler = reactiveHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        return
            route(GET("/"), reactiveHandler::home)
                .and(route(GET("/register"), reactiveHandler::registerView))
                .and(route(GET("/login"), reactiveHandler::loginView))
                .and(route(GET("/logout"), reactiveHandler::logout))
                .and(route(POST("/register"), reactiveHandler::register))
                .and(route(POST("/login"), reactiveHandler::login));
    }

    @Bean
    public RouterFunction<ServerResponse> productRouter() {
        return
            route(GET("/products"), reactiveHandler::listAll)
                .and(route(GET("/save_product"), reactiveHandler::saveView))
                .and(route(POST("/save_product"), reactiveHandler::save));
    }
}