package com.github.xuchen93.demo.r2dbc.schedule;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.PoolMetrics;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xuchen.wang
 * @date 2025/10/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class PoolConnectionMonitorSchedule implements SmartInitializingSingleton {

	private final ConnectionFactory connectionFactory;

	@Scheduled(fixedRate = 1000)
	public void monitorConnectionFacotry() {
		if (connectionFactory instanceof ConnectionPool pool) {
			Optional<PoolMetrics> metrics = pool.getMetrics();
			metrics.ifPresent(m -> {
				log.info("R2DBC连接池监控指标：" +
								" 活跃连接数(acquired): {}" +
								" 总分配连接数(allocated): {}" +
								" 空闲连接数(idle): {}" +
								" 等待队列长度(pending): {}",
						m.acquiredSize(),
						m.allocatedSize(),
						m.idleSize(),
						m.pendingAcquireSize()
				);
			});
		}
	}

	@Override
	public void afterSingletonsInstantiated() {
		if (connectionFactory instanceof ConnectionPool pool) {
			log.info("当前是连接池连接");
			pool.getMetrics().ifPresent(m -> {
				log.info("R2DBC连接池监控指标：" +
								" 最大连接数(maxAllocated): {}" +
								" 最大等待队列长度(maxPending): {}",
						m.getMaxAllocatedSize(),
						m.getMaxPendingAcquireSize()
				);
			});
		} else {
			log.info("当前是普通连接");
		}
	}
}
