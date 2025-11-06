package com.github.xuchen93.demo.r2dbc.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xuchen93.demo.r2dbc.convention.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvisor {

	private ObjectMapper objectMapper = new ObjectMapper();

	@ExceptionHandler(value = Exception.class)
	public Mono<R<Object>> exceptionHandler(Exception exception) {
		log.error("系统错误", exception);
		return Mono.just(R.fail("系统错误"));
	}
}
