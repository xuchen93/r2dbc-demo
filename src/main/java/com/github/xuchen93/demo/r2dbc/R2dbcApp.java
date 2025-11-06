package com.github.xuchen93.demo.r2dbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YangZiYi
 * @description app
 * @date 2025/1/10 18:06
 **/
@Slf4j
@SpringBootApplication
@MapperScan("com.github.xuchen93.demo.r2dbc.table.dao")
public class R2dbcApp {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(R2dbcApp.class, args);
	}
}
