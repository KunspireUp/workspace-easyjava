package com.easyjava.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description:
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午11:13
 */

public class FieldInfo {
	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * bean 属性名称
	 */
	private String propertyName;
	/**
	 * sql 字段类型
	 */
	private String sqlType;
	/**
	 * Java 中的字段类型
	 */
	private String javaType;
	/**
	 * 字段注释
	 */
	private String comment;
	/**
	 * 字段是否自增
	 */
	private Boolean isAutoIncrement;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(Boolean autoIncrement) {
		isAutoIncrement = autoIncrement;
	}
}
