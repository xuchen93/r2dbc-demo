package com.github.xuchen93.demo.r2dbc.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.xuchen93.demo.r2dbc.convention.R;
import com.github.xuchen93.demo.r2dbc.dto.UserImport;
import com.github.xuchen93.demo.r2dbc.table.entity.TestUser;
import io.r2dbc.postgresql.codec.Circle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xuchen.wang
 * @date 2025/10/30
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final R2dbcEntityTemplate entityTemplate;

	private Map<Long, List<Integer>> timeCostMap = new ConcurrentHashMap<>();

	@PostMapping("/import")
	public Mono<R<String>> importTest(@RequestBody UserImport userImport) {
		log.info(entityTemplate.toString());
		if (CollUtil.isEmpty(userImport.getUserList())) {
			return Mono.error(new IllegalArgumentException("用户列表不能为空"));
		}
		long start = System.currentTimeMillis();
		return Flux.fromIterable(userImport.getUserList())
				.buffer(userImport.getBufferSize())
				.concatMap(clueList -> Flux.fromIterable(clueList)
						.flatMap(clue -> entityTemplate.insert(clue), userImport.getConcurrency())
				)
				.then(Mono.just(R.success("导入成功")))
				.doFinally(signalType -> {
					long end = System.currentTimeMillis();
					long cost = end - start;
					timeCostMap.computeIfAbsent(cost, k -> new CopyOnWriteArrayList<>()).add(userImport.getUserList().size());
				});
	}

	@GetMapping("/timeCost")
	public Mono<R<List<String>>> watchTimeCost() {
		log.info("执行timeCost");
		List<String> res = new ArrayList<>();
		int importSize = 0;
		int clueSize = 0;
		long totalCostMs = 0;
		for (Map.Entry<Long, List<Integer>> entry : timeCostMap.entrySet()) {
			importSize += entry.getValue().size();
			clueSize += entry.getValue().stream().mapToInt(i -> i).sum();
			totalCostMs += entry.getKey() * entry.getValue().size();
		}
		if (importSize == 0) {
			res.add("无数据");
			return Mono.just(R.success(res));
		}
		res.add("总导入次数：" + importSize);
		res.add("总数据数量：" + clueSize);
		res.add("总耗时：" + totalCostMs + "ms");
		res.add("平均导入次数耗时：" + (1.0 * totalCostMs / importSize) + "ms");
		res.add("平均每秒导入数据数量：" + (1.0 * clueSize / totalCostMs * 1000.0) + "个");
		res.add("每条数据处理时间：" + (1.0 * totalCostMs / clueSize) + "ms");
		return Mono.just(R.success(res));
	}


	@GetMapping("/timeCostClean")
	public Mono<R<String>> timeCostClean() {
		timeCostMap.clear();
		return Mono.just(R.success());
	}

	@PostMapping("/cleanUser")
	public Mono<R<Long>> cleanUser(@RequestBody UserImport userImport) {
		return entityTemplate.delete(TestUser.class)
				.matching(Query.empty())
				.all()
				.flatMap(l -> Mono.just(R.success(l)));
	}
}
