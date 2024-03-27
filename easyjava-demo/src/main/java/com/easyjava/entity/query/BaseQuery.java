package com.easyjava.entity.query;

/**
 * @Description: 分页参数
 * @Author: KunSpireUp
 * @Date: 16/03/2024 22:28
 */
public class BaseQuery {

	private SimplePage simplePage;
	private Integer pageNo;
	private Integer pageSize;
	private String orderBy;

	private SimplePage getSimplePage() {
		return simplePage;
	}

	public void setSimplePage(SimplePage simplePage) {
		this.simplePage = simplePage;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}

