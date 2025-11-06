package com.github.xuchen93.demo.r2dbc.controller;

import cn.hutool.core.date.DateUtil;
import com.github.xuchen93.demo.r2dbc.convention.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author xuchen.wang
 * @date 2024/11/8
 */
@RestController
public class TestTTController {

	@GetMapping("test")
	public Mono<R<String>> test() {
		return Mono.just(R.success(DateUtil.now()));
	}


	@GetMapping("/getTest")
	public Mono<R<String>> getTest() {
		return Mono.just(R.success(DateUtil.now()));
	}
}
