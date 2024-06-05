package com.easyjava.utils;

import com.easyjava.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description: StringTools
 * @Author: KunSpireUp
 * @Date: 5/12/2024 12:19 AM
 */
public class StringTools {

	public static void checkParam(Object param) {
		try {
			Field[] fields = param.getClass().getDeclaredFields();
			boolean notEmpty = false;
			for (Field field : fields) {
				String methodName = "get" + StringTools.upperCaseFirstLetter(field.getName());
				Method method = param.getClass().getMethod(methodName);
				Object object = method.invoke(param);
				if (object != null && object instanceof java.lang.String && !StringTools.isEmpty(object.toString())
						|| object != null && !(object instanceof java.lang.String)) {
					notEmpty = true;
					break;
				}
			}
			if(!notEmpty) {
				throw  new BusinessException("多参数更新，删除，必须由非空条件");
			}
		}catch (BusinessException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("校验参数是否为空失败");
		}
	}

	public static String upperCaseFirstLetter(String field) {
		if(isEmpty(field)){
			return field;
		}
		// 如果第二个字母是大写，第一个字母不写
		if (field.length() > 1 && Character.isUpperCase(field.charAt(1))) {
			return field;
		}
		return field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	public static boolean isEmpty(String str){
		if(null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
			return true;
		}else if("".equals(str.trim())){
			return true;
		}
		return false;
	}
}
