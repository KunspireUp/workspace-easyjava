package com.easyjava.utils;

/**
 * @Description: 把字母转化为首字母大写和小写
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午11:42
 */
public class StringUtils {

	/**
	 * 把首字母转化为大写
	 * @param field
	 * @return
	 */
	public static String upperCaseFirstLetter(String field){
		if(org.apache.commons.lang3.StringUtils.isEmpty(field)){
			return field;
		}
		return field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	/**
	 * @首字母转化为小写
	 * @param field
	 * @return
	 */
	public static String lowerCaseFirstLetter(String field){
		if(org.apache.commons.lang3.StringUtils.isEmpty(field)){
			return field;
		}
		return field.substring(0, 1).toLowerCase() + field.substring(1);
	}

	public static void main(String[] args) {
		System.out.println(lowerCaseFirstLetter("Company"));
	}
}
