package com.github.xuchen93.demo.r2dbc.config;

import com.github.xuchen93.demo.r2dbc.convention.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author xuchen.wang
 * @date 2025/10/29
 */
@Slf4j
@Component
@Order(0)
public class TraceIdWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String traceId = exchange.getRequest().getHeaders().getFirst(Constants.TRACE_ID_HEADER);
		if (traceId == null || traceId.isEmpty()) {
			traceId = String.format("%s_%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd_HHmmss")), ThreadLocalRandom.current().nextInt(10000, 100000));
		}
		String finalTraceId = traceId;
		return chain.filter(exchange)
				.contextWrite(ctx -> ctx.put(Constants.TRACE_ID, finalTraceId))
				.doOnSubscribe(r -> {
					exchange.getResponse().getHeaders().set(Constants.TRACE_ID_HEADER, finalTraceId);
				})
				;
	}
}
