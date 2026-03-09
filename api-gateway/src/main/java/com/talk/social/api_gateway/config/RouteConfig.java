package com.talk.social.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import com.talk.social.api_gateway.filter.DynamicRateLimitFilter;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

import java.net.URI;

@Configuration
public class RouteConfig {

	@Bean
	public RouterFunction<ServerResponse> gatewayRoutes(DynamicRateLimitFilter dynamicRateLimitFilter) {
		return GatewayRouterFunctions.route("post-service")
                .GET("/api/posts/**", HandlerFunctions.http())
                .filter(dynamicRateLimitFilter)
                .before(uri(URI.create("http://post-service"))) 
                .build();
    
	}
}