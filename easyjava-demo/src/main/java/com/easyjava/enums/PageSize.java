package com.easyjava.enums;

/**
 * @Description: Enum 分页枚举
 * @Author: KunSpireUp
 * @Date: 16/03/2024 22:20
 */
public enum PageSize {

	SIZE15(15),
	SIZE20(20),
	SIZE30(30),
	SIZE40(40),
	SIZE50(50);

	Integer size;

	private PageSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		return size;
	}
}
