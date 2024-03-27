package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @Description: 常量
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午11:24
 */
public class Constants {
	/**
	 * 作者名称
	 */
	public static String COMMENT_AUTHOR;

	/**
	 * 需要忽略的属性
	 */
	public static String IGNORE_BEAN_TOJSON_FIELD = "companyId,status";

	public static String IGNORE_BEAN_TOJSON_EXPRESSION = "@JsonIgnore";

	public static String IGNORE_BEAN_TOJSON_CLASS = "import com.fasterxml.jackson.annotation.JsonIgnore;";

	/**
	 * 日期格式序列化
	 */
	public static String BEAN_DATE_FORMAT_EXPRESSION = "@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")";

	public static String BEAN_DATE_FORMAT_CLASS = "import com.fasterxml.jackson.annotation.JsonFormat;";

	/**
	 * 时间反序列化
	 */
	public static String BEAN_DATE_UNFORMAT_EXPRESSION = "@DateTimeFormat(pattern = \"%s\")";

	public static String BEAN_DATE_UNFORMAT_CLASS = "import org.springframework.format.annotation.DateTimeFormat;";

	/**
	 * 忽略表前缀
	 */
	public static Boolean IGNORE_TABLE_PREFIX;

	/**
	 * bean 参数名后缀
	 */
	public static String SUFFIX_BEAN_QUERY;

	/**
	 * 模糊搜索后缀
	 */
	public static String SUFFIX_BEAN_QUERY_FUZZY;

	/**
	 * 参数日期开始
	 */
	public static String SUFFIX_BEAN_QUERY_TIME_START;

	/**
	 * 参数日期结束
	 */
	public static String SUFFIX_BEAN_QUERY_TIME_END;

	/**
	 * mapper 后缀
	 */
	public static String SUFFIX_MAPPERS;

	/**
	 * 项目路径
	 */
	public static String PATH_BASE;

	/**
	 * 项目包名
	 */
	public static String PACKAGE_BASE;

	/**
	 * 添加 java
	 */
	public static String PATH_JAVA = "java";

	/**
	 * java 资源路径
	 */
	public static String PATH_RESOURCES = "resources";

	/**
	 * po 文件夹路径
	 */
	public static String PATH_PO;

	/**
	 * po 包名
	 */
	public static String PACKAGE_PO;

	/**
	 * query 文件夹路径
	 */
	public static String PATH_QUERY;

	/**
	 * query 包名
	 */
	public static String PACKAGE_QUERY;

	public static String PATH_VO;

	public static String PACKAGE_VO;

	/**
	 * mapper 文件夹路径
	 */
	public static String PATH_MAPPERS;

	/**
	 * mapper 包名
	 */
	public static String PACKAGE_MAPPERS;

	/**
	 * mapper xml 文件路径
	 */
	public static String PATH_MAPPERS_XML;

	/**
	 * utils 文件夹路径
	 */
	public static String PATH_UTILS;

	/**
	 * utils 包名
	 */
	public static String PACKAGE_UTILS;

	/**
	 * enums 枚举文件夹路径
	 */
	public static String PATH_ENUMS;

	/**
	 * enums 枚举包名
	 */
	public static String PACKAGE_ENUMS;

	/**
	 * exception 异常文件夹路径
	 */
	public static String PATH_EXCEPTION;

	/**
	 * exception 异常包名
	 */
	public static String PACKAGE_EXCEPTION;

	/**
	 * service 包名
	 */
	public static String PACKAGE_SERVICE;

	/**
	 * serviceImpl 包名
	 */
	public static String PACKAGE_SERVICE_IMPL;

	/**
	 * service 文件夹路径
	 */
	public static String PATH_SERVICE;

	/**
	 * serviceImpl 文件夹路径
	 */
	public static String PATH_SERVICE_IMPL;

	/**
	 * controller 包名
	 */
	public static String PACKAGE_CONTROLLER;

	/**
	 * controller 文件夹路径
	 */
	public static String PATH_CONTROLLER;


	static {
		/** ----------------------获取 application.properties 配置---------------------------------*/

		/** 作者 */
		COMMENT_AUTHOR = String.valueOf(Boolean.valueOf(PropertiesUtils.getString("comment.author")));

		/** 需要忽略的属性 */
		IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
		IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
		IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");

		/** 时间格式序列化 */
		BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
		BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");

		/** 时间反序列化 */
		BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.unformat.expression");
		BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unformat.class");

		/** 是否忽略前缀 */
		IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));

		/** 参数 bean 的后缀 */
		SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");

		/** 模糊搜索后缀 */
		SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");

		/** 参数日期开始后缀 */
		SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");

		/** 参数日期结束后缀 */
		SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");

		/** mapper 后缀 */
		SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");


		/** ---------------------------------------------------------------------------------------*/
		/** 文件输出路径 D:/Develop/work-develop/workspace-easyjava/easyjava-demo/src/main/ */
		PATH_BASE = PropertiesUtils.getString("path.base");
		/** D:/Develop/work-develop/workspace-easyjava/easyjava-demo/src/main/java/com.easyjava */
		PATH_BASE = PATH_BASE + PATH_JAVA + "/" + PropertiesUtils.getString("package.base");
		/** 将 . 替换为 / */
		PATH_BASE = PATH_BASE.replace(".", "/");
		/** ---------------------------------------------------------------------------------------*/

		/** 包名 com.easyjava */
		PACKAGE_BASE = PropertiesUtils.getString("package.base");

		/** PO 包名 com.easyjava.entity.po */
		PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");

		/** PO 的文件夹路径 */
		PATH_PO = PATH_BASE + "/" + PropertiesUtils.getString("package.po").replace(".", "/");

		/** query 包名 com.easyjava.query */
		PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");

		/** query 的文件夹路径 */
		PATH_QUERY = PATH_BASE + "/" + PropertiesUtils.getString("package.query").replace(".", "/");

		/** vo 包名 */
		PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");

		/** vo 的文件夹路径 */
		PATH_VO = PATH_BASE + "/" + PropertiesUtils.getString("package.vo").replace(".", "/");

		/** mapper com.easyjava.mapper */
		PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");

		/** mapper 文件夹路径 */
		PATH_MAPPERS = PATH_BASE + "/" + PropertiesUtils.getString("package.mappers").replace(".", "/");

		PATH_MAPPERS_XML = PropertiesUtils.getString("path.base") + PATH_RESOURCES + "/" + PACKAGE_MAPPERS.replace(".", "/");

		/** utils 包名 com.easyjava.utils */
		PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");

		/** utils 文件夹路径 */
		PATH_UTILS = PATH_BASE + "/" + PropertiesUtils.getString("package.utils").replace(".", "/");

		/** enum 包名 com.easyjava.enums */
		PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");

		/** enum 文件夹路径 */
		PATH_ENUMS = PATH_BASE + "/" + PropertiesUtils.getString("package.enums").replace(".", "/");

		/** 异常包名 com.easyjava.exception */
		PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");

		/** 异常文件夹路径 */
		PATH_EXCEPTION = PATH_BASE + "/" + PropertiesUtils.getString("package.exception").replace(".", "/");

		/** service 包名 */
		PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");

		/** serviceImpl 包名 */
		PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");

		/** service 文件夹路径 */
		PATH_SERVICE = PATH_BASE + "/" + PropertiesUtils.getString("package.service").replace(".", "/");

		/** serviceImpl 文件夹路径 */
		PATH_SERVICE_IMPL = PATH_BASE + "/" + PropertiesUtils.getString("package.service.impl").replace(".", "/");

		/** controller 包名 */
		PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");

		/** controller 文件夹路径 */
		PATH_CONTROLLER = PATH_BASE + "/" + PropertiesUtils.getString("package.controller").replace(".", "/");
		
	}

	public static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
	public static String[] SQL_DATE_TYPES = new String[]{"date"};
	public static String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};
	public static String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
	public static String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint"};
	public static String[] SQL_LONG_TYPES = new String[]{"bigint"};

	public static void main(String[] args) {
		System.out.println(PATH_BASE);
		System.out.println(PACKAGE_BASE);
		System.out.println(PACKAGE_PO);
		System.out.println(PATH_PO);
		System.out.println(PATH_VO);
		System.out.println(PACKAGE_VO);
		System.out.println(PACKAGE_UTILS);
		System.out.println(PATH_MAPPERS_XML);
		System.out.println(PACKAGE_SERVICE);
		System.out.println(PACKAGE_SERVICE_IMPL);
		System.out.println(PATH_SERVICE);
		System.out.println(PATH_SERVICE_IMPL);
	}
}
