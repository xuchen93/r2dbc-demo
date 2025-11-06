package com.github.xuchen93.demo.r2dbc.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xuchen93.demo.r2dbc.convention.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvisor {

	private ObjectMapper objectMapper = new ObjectMapper();

	@ExceptionHandler(value = Exception.class)
	public R<Object> exceptionHandler(Exception exception) {
		log.error("系统错误", exception);
		return R.fail("系统错误");
	}
}
