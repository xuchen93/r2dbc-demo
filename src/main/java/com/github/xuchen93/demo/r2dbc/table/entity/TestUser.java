package com.github.xuchen93.demo.r2dbc.table.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * <p>
 *
 * </p>
 *
 * @author xuchen.wang
 * @since 2025-10-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("test_user")
public class TestUser {

	/**
	 * 主键ID
	 * 对应字段：id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 用户名(唯一)
	 * 对应字段：username
	 */
	private String username;

	/**
	 * 昵称
	 * 对应字段：nickname
	 */
	private String nickname;

	/**
	 * 年龄
	 * 对应字段：age
	 */
	private Integer age;

	/**
	 * 性别(0:未知 1:男 2:女)
	 * 对应字段：gender
	 */
	private Integer gender;

	/**
	 * 邮箱
	 * 对应字段：email
	 */
	private String email;

	/**
	 * 手机号
	 * 对应字段：phone
	 */
	private String phone;

	/**
	 * 状态(0:禁用 1:正常)
	 * 对应字段：status
	 */
	private Integer status;

	/**
	 * 创建人
	 * 对应字段：created_by
	 */
	private String createdBy;

	/**
	 * 更新人
	 * 对应字段：updated_by
	 */
	private String updatedBy;

	/**
	 * 删除标识(0:未删除 1:已删除)
	 * 对应字段：deleted
	 */
	private Integer deleted;
}
