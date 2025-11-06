package com.github.xuchen93.demo.r2dbc.advisor;

import com.github.xuchen93.demo.r2dbc.convention.Constants;
import com.github.xuchen93.demo.r2dbc.convention.R;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * @author xuchen.wang
 * @date 2025/7/15
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class FluxRequestLogAdvisor {


	private final ObjectMapper objectMapper = new ObjectMapper();

	private final int maxMsgLength = 2000;

	@PostConstruct
	public void init() {
		log.info("【注册】{}", this.getClass().getSimpleName());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}


	// 定义切点
	@Pointcut("execution(* *..controller..*.*(..))")
	public void controllerPointCut() {

	}

	// Around增强处理
	@Around("controllerPointCut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取方法信息

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		int argLength = joinPoint.getArgs().length;
		Object arg;
		switch (argLength) {
			case 0 -> arg = null;
			case 1 -> arg = joinPoint.getArgs()[0];
			default -> arg = joinPoint.getArgs();
		}
		long startTime = System.currentTimeMillis();
		String requestParams = objectMapper.writeValueAsString(arg);

		// 执行目标方法
		Mono<R> result = (Mono<R>) joinPoint.proceed();
		StringBuffer traceId = new StringBuffer(30);
		// 处理响应
		return result.deferContextual(contextView -> {
					// 从 Context 中获取上游的 traceId
					traceId.append(contextView.getOrDefault(Constants.TRACE_ID, "UNKNOWN"));
					log.info("[{}]请求[{}]参数：{}", traceId, method.getName(), requestParams);
					return result;
				})
				.map(r -> {
					r.setTraceId(traceId.toString());
					return r;
				})
				.doOnSuccess(response -> log.info("[{}]耗时[{}]ms出参:[{}]", traceId, System.currentTimeMillis() - startTime, formatObject(response)))
				.doOnError(error -> log.error("[{}]失败：{}", traceId, error.getMessage()))
				;
	}

	private String formatObject(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			String str = objectMapper.writeValueAsString(obj);
			if (str.length() > maxMsgLength) {
				str = str.substring(0, maxMsgLength);
			}
			return str;
		} catch (JsonProcessingException e) {
			log.error("格式化失败：{}", e.getMessage());
		}
		return "";
	}
}
