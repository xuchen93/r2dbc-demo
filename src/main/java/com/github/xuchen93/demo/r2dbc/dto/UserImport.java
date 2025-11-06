package com.github.xuchen93.demo.r2dbc.dto;


import com.github.xuchen93.demo.r2dbc.table.entity.TestUser;
import lombok.Data;

import java.util.List;

/**
 * @author xuchen.wang
 * @date 2025/10/30
 */
@Data
public class UserImport {
	private Integer bufferSize;
	private Integer concurrency;
	private Long waitTime;
	private List<TestUser> userList;
}
