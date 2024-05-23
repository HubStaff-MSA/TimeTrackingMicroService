package com.hubstaffmicroservices.msagateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class BroadcastFilter extends AbstractGatewayFilterFactory<BroadcastFilter.Config> {

    private final WebClient webClient;

    public BroadcastFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    public static class Config {
        // Configuration properties for the filter if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Mono<Void> forwardToStudents = forwardRequest(exchange, "http://localhost:8090");
            Mono<Void> forwardToControlTrackTime = forwardRequest(exchange, "http://localhost:8050");

            return Mono.when(forwardToStudents, forwardToControlTrackTime).then(chain.filter(exchange));
        };
    }

    private Mono<Void> forwardRequest(ServerWebExchange exchange, String uri) {
        return webClient.method(HttpMethod.valueOf(String.valueOf(exchange.getRequest().getMethod())))
                .uri(uri + exchange.getRequest().getURI().getPath())
                .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
                .body((clientHttpRequest, context) -> exchange.getRequest().getBody().then())
                .retrieve()
                .bodyToMono(Void.class)
                .then();
    }
}
