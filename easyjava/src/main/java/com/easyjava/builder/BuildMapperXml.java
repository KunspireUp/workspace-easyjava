package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import jdk.jfr.internal.EventWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KunSpireUp
 * @Date: 14/03/2024 23:31
 */
public class BuildMapperXml {

	private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

	private static final String BASE_COLUMN_LIST = "base_column_list";

	private static final String BASE_QUERY_CONDITION = "base_query_condition";

	private static final String BASE_QUERY_CONDITION_EXTENDED = "base_query_condition_extend";

	private static final String QUERY_CONDITION = "query_condition";

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_MAPPERS_XML);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
		File poFile = new File(folder, className + ".xml");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		FieldInfo idField = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/***********************************************************
			 * MapperXml 文件头头部
			 ***********************************************************/
			{
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
				bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n");
				bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">\n");
				String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();

				/***********************************************************
				 * 生成实体映射
				 ***********************************************************/
				{
					bw.write("\t<!-- 实体映射-->");
					bw.newLine();
					bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">\n");

					Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
					for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
						if ("PRIMARY".equals(entry.getKey())) {
							List<FieldInfo> fieldInfoList = entry.getValue();
							if (fieldInfoList.size() == 1) {
								idField = fieldInfoList.get(0);
								break;
							}
						}
					}
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						bw.write("\t\t<!--" + fieldInfo.getComment() + "-->");
						bw.newLine();
						String key = "";
						if (idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
							key = "id";
						} else {
							key = "result";
						}
						bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>\n");
					}
					bw.write("\t</resultMap>\n\n");
				}

				/***********************************************************
				 * 生成通用查询结果列
				 ***********************************************************/
				{
					bw.write("\t<!-- 通用查询结果列 -->\n");
					bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">\n");
					StringBuilder columnBuilder = new StringBuilder();
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						columnBuilder.append(fieldInfo.getFieldName()).append(",");
					}
					String columnBuilderStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));
					bw.write("\t\t" + columnBuilderStr + "\n");
					bw.write("\t</sql>\n\n");

					/** 生成基础查询条件 */
					bw.write("\t<!-- 基础查询条件 -->\n");
					bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">\n");

					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						String stringQuery = "";
						if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
							stringQuery = " and query." + fieldInfo.getPropertyName() + "!= ''";
						}
						bw.write("\t\t<if test=\" query." + fieldInfo.getPropertyName() + " != null" + stringQuery + "\">\n");
						bw.write("\t\t\tand id = #{query." + fieldInfo.getPropertyName() + "}\n");
						bw.write("\t\t</if>\n");

					}
					bw.write("\t</sql>\n\n");
				}

				/***********************************************************
				 * 生成扩展查询条件
				 ***********************************************************/
				{
					bw.write("\t<!-- 扩展查询条件 -->\n");
					bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTENDED + "\">\n");

					for (FieldInfo fieldInfo : tableInfo.getFieldExtendList()) {
						String andWhere = "";
						if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
							andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')";
						} else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
							if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
								andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
							} else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
								andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day) ]]>";
							}
						}
						bw.write("\t\t<if test=\" query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + "!= null \">\n");
						bw.write("\t\t\t" + andWhere + "\n");
						bw.write("\t\t</if>\n");
					}
					bw.write("\t</sql>\n\n");
				}

				/***********************************************************
				 * 生成扩展查询条件
				 ***********************************************************/
				{
					bw.write("\t<!-- 通用查询条件 -->\n");
					bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">\n");
					bw.write("\t\t<where>\n");
					bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>\n");
					bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTENDED + "\"/>\n");
					bw.write("\t\t</where>\n");
					bw.write("\t</sql>\n\n");
				}

				/***********************************************************
				 * 生成查询列表
				 ***********************************************************/
				{
					bw.write("\t<!-- 查询列表 -->\n");
					bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">\n");
					bw.write("\t\tSELECT \n\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>\n\t\t\tFROM " + tableInfo.getTableName() + "\n\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");
					bw.write("\t\t<if test=\"query.orderBy != null\">\n\t\t\torder by ${query.orderBy}\n\t\t</if>\n");
					bw.write("\t\t<if test=\"query.simplePage != null\">\n\t\t\tlimit ${query.simplePage.start}, ${query.simplePage.end}\n\t\t</if>\n");
					bw.write("\t</select>\n\n");
				}


				/***********************************************************
				 * 生成查询数量
				 ***********************************************************/
				{
					bw.write("\t<!-- 查询数量 -->\n");
					bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">\n");
					bw.write("\t\tSELECT COUNT(1) FROM " + tableInfo.getTableName() + "\n");
					bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");
					bw.write("\t</select>\n\n");
				}

				/***********************************************************
				 * 单条插入数据
				 ***********************************************************/
				{
					bw.write("\t<!-- 单条插入 (匹配有值的字段) -->\n");
					bw.write("\t<insert id=\"insert\" parameterType=\"" + poClass + "\">\n");

					FieldInfo autoIncrementFiled = null;
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						if (fieldInfo.getAutoIncrement() != null && fieldInfo.getAutoIncrement()) {
							autoIncrementFiled = fieldInfo;
							break;
						}
					}
					if (autoIncrementFiled != null) {
						bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementFiled.getFieldName() + "\" resultType=\"" + autoIncrementFiled.getJavaType() + "\" order=\"AFTER\">\n");
						bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
						bw.write("\t\t</selectKey>\n");
					}
					bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
					/** trim prefix 标签 1 */
					{
						bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							bw.write("\t\t\t<if test=\"" + "bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</trim>\n");
					}
					/** trim prefix 标签 2 */
					{
						bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							bw.write("\t\t\t<if test=\"" + "bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</trim>\n");
					}

					bw.write("\t</insert>\n\n");
				}

				/***********************************************************
				 * 插入或者更新（匹配有值的字段）
				 ***********************************************************/
				{
					bw.write("\t<!-- 插入或者更新（匹配有值的字段） -->\n");
					bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">\n");

					bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "\n");
					/** trim prefix 标签 1 */
					{
						bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</trim>\n");
					}
					/** trim prefix 标签 2 */
					{
						bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</trim>\n");
					}
					bw.write("\t\tON DUPLICATE KEY UPDATE\n");
					Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
					Map<String, String> keyTempMap = new HashMap<>();
					for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
						List<FieldInfo> fieldInfoList = entry.getValue();
						for (FieldInfo item : fieldInfoList) {
							keyTempMap.put(item.getFieldName(), item.getFieldName());
						}
					}
					/** trim prefix 标签 3 */
					{
						bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">\n");
						// 如果有主键不存在，就 continue
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							if (keyTempMap.get(fieldInfo.getFieldName()) != null) {
								continue;
							}
							bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</trim>\n");
					}
					bw.write("\t</insert>\n\n");
				}

				/***********************************************************
				 * 添加（批量添加）
				 ***********************************************************/
				{
					bw.write("\t<!-- 添加（批量添加） -->\n");
					bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + poClass + "\" >\n");
					StringBuilder insertFieldBuffer = new StringBuilder();
					StringBuffer insertPropertyBuffer = new StringBuffer();
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						if (fieldInfo.getAutoIncrement()) {
							continue;
						}
						insertFieldBuffer.append(fieldInfo.getFieldName()).append(", ");

						insertPropertyBuffer.append("#{item." + fieldInfo.getPropertyName() + "}").append(", ");
					}
					String insertFieldBufferStr = insertFieldBuffer.substring(0, insertFieldBuffer.lastIndexOf(","));
					bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldBufferStr + ") values\n");
					bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");

					String insertPropertyBufferStr = insertPropertyBuffer.substring(0, insertPropertyBuffer.lastIndexOf(","));
					bw.write("\t\t\t(" + insertPropertyBufferStr + ")\n");
					bw.write("\t\t</foreach>\n");
					bw.write("\t</insert>\n\n");
				}

				/***********************************************************
				 * 批量新增修改（批量插入）
				 ***********************************************************/
				{
					bw.write("\t<!-- 批量新增修改（批量插入） -->\n");
					bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClass + "\" >\n");
					StringBuilder insertFieldBuffer = new StringBuilder();
					StringBuffer insertPropertyBuffer = new StringBuffer();
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						if (fieldInfo.getAutoIncrement()) {
							continue;
						}
						insertFieldBuffer.append(fieldInfo.getFieldName()).append(", ");

						insertPropertyBuffer.append("#{item." + fieldInfo.getPropertyName() + "}").append(", ");
					}
					String insertFieldBufferStr = insertFieldBuffer.substring(0, insertFieldBuffer.lastIndexOf(","));
					bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + "(" + insertFieldBufferStr + ") values\n");
					bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">\n");

					String insertPropertyBufferStr = insertPropertyBuffer.substring(0, insertPropertyBuffer.lastIndexOf(","));
					bw.write("\t\t\t(" + insertPropertyBufferStr + ")\n");
					bw.write("\t\t</foreach>\n");

					bw.write("\t\tON DUPLICATE KEY UPDATE\n");
					StringBuffer insertOrUpdateBatchBuffer = new StringBuffer();
					for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
						insertOrUpdateBatchBuffer.append("\t\t" + fieldInfo.getFieldName() + " =  VALUES(" + fieldInfo.getFieldName() + "),\n");
					}
					String insertOrUpdateBatchStr = insertOrUpdateBatchBuffer.substring(0, insertOrUpdateBatchBuffer.lastIndexOf(","));
					bw.write(insertOrUpdateBatchStr + "\n");
					bw.write("\t</insert>\n\n");
				}

				/***********************************************************
				 * 根据 参数查询
				 ***********************************************************/
				{
					Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
					for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
						List<FieldInfo> keyFieldInfoList = entry.getValue();

						Integer index = 0;
						StringBuilder methodName = new StringBuilder();

						StringBuilder methodParam = new StringBuilder();

						for (FieldInfo fieldInfo : keyFieldInfoList) {
							index++;
							methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
							methodParam.append(fieldInfo.getFieldName() + " =#{" + fieldInfo.getPropertyName() + "}");
							if (index < keyFieldInfoList.size()) {
								methodName.append("And");
								methodParam.append(" and ");
							}
						}

						/** 查询 */
						bw.write("\t<!-- 根据 " + methodName + " 查询 -->\n");
						bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">\n");
						bw.write("\t\tselect\n\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/> \n\t\tfrom " + tableInfo.getTableName() + " where " + methodParam + "\n");
						bw.write("\t</select>\n\n");

						/** 更新 */
						bw.write("\t<!-- 根据 " + methodName + " 更新 -->\n");
						bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + poClass + "\">\n");
						bw.write("\t\tupdate " + tableInfo.getTableName() + "\n");
						bw.write("\t\t<set>\n");
						for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
							bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">\n");
							bw.write("\t\t\t\t" + fieldInfo.getFieldName() + "= #{bean." + fieldInfo.getPropertyName() + "},\n");
							bw.write("\t\t\t</if>\n");
						}
						bw.write("\t\t</set>\n");
						bw.write("\t\twhere " + methodParam + "\n");
						bw.write("\t</update>\n\n");

						/** 删除 */
						bw.write("\t<!-- 根据 " + methodName + " 删除 -->\n");
						bw.write("\t<delete id=\"deleteBy" + methodName + "\">\n");
						bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + methodParam + "\n");
						bw.write("\t</delete>\n\n");
					}
				}
				bw.write("</mapper>");
				bw.flush();
			}
		} catch (Exception e) {
			logger.info("创建 mapper.xml 失败");
		} finally {
			if (outWriter != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (outWriter != null) {
				try {
					outWriter.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
