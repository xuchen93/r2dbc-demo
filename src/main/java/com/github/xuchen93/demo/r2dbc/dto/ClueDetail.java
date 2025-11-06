package com.github.xuchen93.demo.r2dbc.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuchen.wang
 * @date 2025/10/30
 */
@Data
public class ClueDetail {
	private String phone;
	private String unikey;
	private String customerName;
	private String remark;
	private Map<String, String> variables = new HashMap<>();
}
