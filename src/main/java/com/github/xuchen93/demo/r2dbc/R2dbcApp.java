package com.github.xuchen93.demo.r2dbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author YangZiYi
 * @description app
 * @date 2025/1/10 18:06
 **/
@Slf4j
@SpringBootApplication
@EnableScheduling
public class R2dbcApp {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(R2dbcApp.class, args);
	}
}
