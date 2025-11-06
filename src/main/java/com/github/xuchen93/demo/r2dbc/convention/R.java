package com.github.xuchen93.demo.r2dbc.convention;

import lombok.Data;

import java.io.Serializable;


@Data
public class R<T> implements Serializable {

	private String traceId;

	private int code;

	private String msg;

	private T data;


	public R() {

	}

	private R(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> R<T> success() {
		return success(200, null);
	}


	public static <T> R<T> success(T data) {
		return new R<>(200, "success", data);
	}

	public static <T> R<T> success(int code, T data) {
		return new R<>(code, "success", data);
	}

	public static <T> R<T> successMsg(String msg) {
		return new R<>(200, msg, null);
	}


	public static <T> R<T> success(int code, String msg, T data) {
		return new R<>(code, msg, data);
	}

	public static <T> R<T> fail() {
		return fail("error");
	}

	public static <T> R<T> fail(String msg) {
		return fail(400, msg);
	}

	public static <T> R<T> fail(int code, String msg) {
		return new R<>(code, msg, null);
	}

	public static <T> R<T> fail(int code, String msg, T data) {
		return new R<>(code, msg, data);
	}
}
