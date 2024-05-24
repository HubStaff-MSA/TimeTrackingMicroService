//package com.hubstaffmicroservices.msagateway;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class DynamicBroadcastFilter implements GatewayFilterFactory {
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> {
//            String path = exchange.getRequest().getPath().toString();
//
//            // List of target URIs based on the incoming path
//            Map<String, List<String>> routeMappings = Map.of(
//                    "/api/v2/control/freeze", List.of(
//                            "http://localhost:8050/api/v2/control/tracktime/freeze",
//                            "http://localhost:8090/api/v2/control/project/freeze"
//                    )
//                    // Add other mappings here as needed
//            );
//
//            List<String> targetUris = routeMappings.getOrDefault(path, List.of());
//
//            if (targetUris.isEmpty()) {
//                return chain.filter(exchange);
//            }
//
//            WebClient webClient = webClientBuilder.build();
//
//            Mono<Void> aggregatedResponse = Mono.when(
//                    targetUris.stream()
//                            .map(uri -> webClient.post()
//                                    .uri(uri)
//                                    .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
//                                    .body(exchange.getRequest().getBody(), String.class)
//                                    .retrieve()
//                                    .bodyToMono(Void.class)
//                                    .then())
//                            .toArray(Mono[]::new)
//            );
//
//            return aggregatedResponse.then(chain.filter(exchange));
//        };
//    }
//
//}
