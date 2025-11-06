package com.github.xuchen93.demo.r2dbc.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xuchen93.demo.r2dbc.convention.R;
import com.github.xuchen93.demo.r2dbc.dto.UserImport;
import com.github.xuchen93.demo.r2dbc.table.service.TestUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	private final TestUserService testUserService;

	private Map<Long, List<Integer>> timeCostMap = new ConcurrentHashMap<>();

	@PostMapping("/import")
	public R<String> importTest(@RequestBody UserImport userImport) {
		if (CollUtil.isEmpty(userImport.getUserList())) {
			throw new IllegalArgumentException("用户列表不能为空");
		}
		long start = System.currentTimeMillis();
		userImport.getUserList().forEach(user -> {
			testUserService.save(user);
		});
		long end = System.currentTimeMillis();
		List<Integer> sizeList = timeCostMap.computeIfAbsent(end - start, k -> new CopyOnWriteArrayList<>());
		sizeList.add(userImport.getUserList().size());
		return R.success();
	}

	@GetMapping("/timeCost")
	public R<List<String>> watchTimeCost() {
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
			return R.success(res);
		}
		res.add("总导入次数：" + importSize);
		res.add("总数据数量：" + clueSize);
		res.add("总耗时：" + totalCostMs + "ms");
		res.add("平均导入次数耗时：" + (1.0 * totalCostMs / importSize) + "ms");
		res.add("平均每秒导入数据数量：" + (1.0 * clueSize / totalCostMs * 1000.0) + "个");
		res.add("每条数据处理时间：" + (1.0 * totalCostMs / clueSize) + "ms");
		return R.success(res);
	}


	@GetMapping("/timeCostClean")
	public R<String> timeCostClean() {
		timeCostMap.clear();
		return R.success();
	}

	@PostMapping("/cleanUser")
	public R<Boolean> cleanUser(@RequestBody UserImport userImport) {
		boolean remove = testUserService.remove(new QueryWrapper<>());
		return R.success(remove);
	}
}
