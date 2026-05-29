package com.lifeops.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserContextHeaderFilter implements GlobalFilter, Ordered {

    public static final String USER_EXTERNAL_ID_HEADER = "X-User-External-Id";
    public static final String USER_EMAIL_HEADER = "X-User-Email";
    public static final String USER_NAME_HEADER = "X-User-Name";
    public static final String AUTH_PROVIDER_HEADER = "X-Auth-Provider";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .map(authentication -> {
                    Jwt jwt = authentication.getToken();

                    String externalId = jwt.getSubject();
                    String email = jwt.getClaimAsString("email");
                    String name = jwt.getClaimAsString("name");

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .headers(headers -> {
                                headers.remove(USER_EXTERNAL_ID_HEADER);
                                headers.remove(USER_EMAIL_HEADER);
                                headers.remove(USER_NAME_HEADER);
                                headers.remove(AUTH_PROVIDER_HEADER);

                                headers.add(USER_EXTERNAL_ID_HEADER,safeValue(externalId));
                                headers.add(USER_EMAIL_HEADER,safeValue(email));
                                headers.add(USER_NAME_HEADER,safeValue(name));
                                headers.add(AUTH_PROVIDER_HEADER,"GOOGLE");
                            })
                            .build();

                    return exchange.mutate()
                            .request(mutatedRequest)
                            .build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private String safeValue(String value) {
        return value == null ? "" : value;
    }
}
