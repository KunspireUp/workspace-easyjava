package com.easyjava.entity.query;

import java.util.Date;

import java.math.BigDecimal;

/**
 * @Description: 商品信息
 * @Author: false
 * @Date: 2024/06/05 19:02:23
 */
public class ProductInfoQuery extends BaseQuery {
	/**
 	 * 自增ID 查询对象
 	 */
	private Integer id;

	/**
 	 * 公司ID 查询对象
 	 */
	private String companyId;

	private String companyIdFuzzy;

	/**
 	 * 商品编号 查询对象
 	 */
	private String code;

	private String codeFuzzy;

	/**
 	 * 商品名称 查询对象
 	 */
	private String productName;

	private String productNameFuzzy;

	/**
 	 * 价格 查询对象
 	 */
	private BigDecimal price;

	/**
 	 * sku类型 查询对象
 	 */
	private Integer skuType;

	/**
 	 * 颜色类型 查询对象
 	 */
	private Integer colorType;

	/**
 	 * 创建时间 查询对象
 	 */
	private Date createTime;

	private String createTimeStart;
	private String createTimeEnd;
	/**
 	 * 创建日期 查询对象
 	 */
	private Date createDate;

	private String createDateStart;
	private String createDateEnd;
	/**
 	 * 库存 查询对象
 	 */
	private Long stock;

	/**
 	 * 状态 查询对象
 	 */
	private Integer status;


	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}

	public Integer getSkuType() {
		return skuType;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	}

	public Integer getColorType() {
		return colorType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Long getStock() {
		return stock;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setCompanyIdFuzzy(String companyIdFuzzy) {
		this.companyIdFuzzy = companyIdFuzzy;
	}

	public String getCompanyIdFuzzy() {
		return companyIdFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy() {
		return codeFuzzy;
	}

	public void setProductNameFuzzy(String productNameFuzzy) {
		this.productNameFuzzy = productNameFuzzy;
	}

	public String getProductNameFuzzy() {
		return productNameFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}
}