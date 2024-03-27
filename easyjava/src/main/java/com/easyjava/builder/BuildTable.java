package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.PropertiesUtils;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 读取表数据
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午9:17
 */
public class BuildTable {

	private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

	private static Connection conn = null;
	//可以获取 table 的更多信息
	private static final String SQL_SHOW_TABLE_STATUS = "show table status";

	private static final String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";

	private static final String SQL_SHOW_TABLE_INDEX = "show index from %s";

	static {
		String driverName = PropertiesUtils.getString("db.driver.name");
		String url = PropertiesUtils.getString("db.url");
		String username = PropertiesUtils.getString("db.username");
		String password = PropertiesUtils.getString("db.password");
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			logger.error("数据库连接失败", e);
		}
	}

	/**
	 * 读取表基础信息
	 *
	 * @return
	 * @return
	 */
	public static List<TableInfo> getTable() {
		PreparedStatement ps = null;
		ResultSet tableResult = null;

		List<TableInfo> tableInfoList = new ArrayList();

		List<FieldInfo> fieldInfoList = new ArrayList();

		try {
			ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
			tableResult = ps.executeQuery();
			while (tableResult.next()) {
				String tableName = tableResult.getString("name");
				String comment = tableResult.getString("comment");

				String beanName = tableName;
				if (Constants.IGNORE_TABLE_PREFIX) {
					beanName = tableName.substring(beanName.indexOf("_") + 1);
				}
				beanName = processField(beanName, true);

				TableInfo tableInfo = new TableInfo();
				tableInfo.setTableName(tableName);
				tableInfo.setBeanName(beanName);
				tableInfo.setComment(comment);
				tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);

				readFieldInfo(tableInfo);
				getKeyIndexInfo(tableInfo);

				tableInfoList.add(tableInfo);


				//logger.info("tableInfo:{}", JSON.toJSONString(tableInfo, SerializerFeature.DisableCircularReferenceDetect));
				//出现对象重复出现多次
				//logger.info("tableInfo:{}", JsonUtils.convertObjectToJSON(tableInfo));

				//logger.info("bean:{}", beanName);
                /*logger.info("表:{}, 备注:{}, JavaBean:{}, JavaParamBean:{}", tableInfo.getTableName(),
                        tableInfo.getComment(), tableInfo.getBeanName(), tableInfo.getBeanParamName());*/
				//logger.info("表:{}", JsonUtils.beanToJson(tableInfo));
				//logger.info("字段:{}", JsonUtils.beanToJson(fieldInfoList));
			}
		} catch (Exception e) {
			logger.error("读取表失败", e);

		} finally {
			if (tableResult != null) {
				try {
					tableResult.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return tableInfoList;
	}

	/**
	 * Process读取字段信息
	 *
	 * @param tableInfo
	 * @return
	 */
	private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
		PreparedStatement ps = null;
		ResultSet fieldResult = null;

		List<FieldInfo> fieldInfoList = new ArrayList<>();

		List<FieldInfo> fieldExtendList = new ArrayList<>();
		try {
			ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
			fieldResult = ps.executeQuery();

			Boolean haveDateTime = false;
			Boolean haveDate = false;
			Boolean haveBigDecimal = false;
			while (fieldResult.next()) {
				String field = fieldResult.getString("field");
				String type = fieldResult.getString("type");
				String extra = fieldResult.getString("extra");
				String comment = fieldResult.getString("comment");
				//logger.info("field:{},type:{}, extra:{}, comment:{}", field, type, extra, comment);
				if (type.indexOf("(") > 0) {
					type = type.substring(0, type.indexOf("("));
				}
				String propertyName = processField(field, false);
				FieldInfo fieldInfo = new FieldInfo();

				fieldInfo.setFieldName(field);
				fieldInfo.setComment(comment);
				fieldInfo.setSqlType(type);
				fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra) ? true : false);
				fieldInfo.setPropertyName(propertyName);
				fieldInfo.setJavaType(processJavaType(type));

				fieldInfoList.add(fieldInfo);
				tableInfo.setFieldList(fieldInfoList);

				//logger.info("javaType:{}", fieldInfo.getJavaType());

				if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
					haveDateTime = true;
				}
				if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
					haveDate = true;
				}
				if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
					haveBigDecimal = true;
				}

				/** String 类型 */
				if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)) {

					FieldInfo fuzzyField = new FieldInfo();
					fuzzyField.setJavaType(fieldInfo.getJavaType());
					fuzzyField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_FUZZY);
					fuzzyField.setFieldName(fieldInfo.getFieldName());
					fuzzyField.setSqlType(type);
					fieldExtendList.add(fuzzyField);
				}

				/** 日期类型的参数起止 */
				if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
					FieldInfo timeStartField = new FieldInfo();
					timeStartField.setJavaType("String");
					timeStartField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_TIME_START);
					timeStartField.setFieldName(fieldInfo.getFieldName());
					timeStartField.setSqlType(type);
					fieldExtendList.add(timeStartField);

					FieldInfo timeEndField = new FieldInfo();
					timeEndField.setJavaType("String");
					timeEndField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_TIME_END);
					timeEndField.setFieldName(fieldInfo.getFieldName());
					timeEndField.setSqlType(type);
					fieldExtendList.add(timeEndField);
				}
			}
			tableInfo.setHaveDateTime(haveDateTime);
			tableInfo.setHaveDate(haveDate);
			tableInfo.setHaveBigDecimal(haveBigDecimal);
			tableInfo.setFieldList(fieldInfoList);
			tableInfo.setFieldExtendList(fieldExtendList);
		} catch (Exception e) {
			logger.error("读取表失败", e);

		} finally {
			if (fieldResult != null) {
				try {
					fieldResult.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return fieldInfoList;
	}

	/**
	 * getKey 读取索引信息
	 *
	 * @param tableInfo
	 */
	private static void getKeyIndexInfo(TableInfo tableInfo) {
		PreparedStatement ps = null;
		ResultSet fieldResult = null;

		Map<String, FieldInfo> tempMap = new HashMap();
		for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
			tempMap.put(fieldInfo.getFieldName(), fieldInfo);
		}

		try {
			ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
			fieldResult = ps.executeQuery();
			while (fieldResult.next()) {
				String keyName = fieldResult.getString("key_name");
				Integer nonUnique = fieldResult.getInt("non_unique");
				String columnName = fieldResult.getString("column_name");
				if (nonUnique == 1) {
					continue;
				}
				List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
				if (null == keyFieldList) {
					keyFieldList = new ArrayList();
					tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
				}
				/*for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
					if (fieldInfo.getFieldName().equals(columnName)) {
						keyFieldList.add(fieldInfo);
					}
				}*/
				keyFieldList.add(tempMap.get(columnName));

			}
		} catch (Exception e) {
			logger.error("读取索引失败", e);

		} finally {
			if (fieldResult != null) {
				try {
					fieldResult.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 处理字段,将首字母转为大写
	 *
	 * @param field
	 * @param upperCaseFirstLetter
	 * @return
	 */
	private static String processField(String field, Boolean upperCaseFirstLetter) {
		StringBuffer sb = new StringBuffer();
		String[] fields = field.split("_");
		sb.append(upperCaseFirstLetter ? StringUtils.upperCaseFirstLetter(fields[0]) : fields[0]);
		for (int i = 1, len = fields.length; i < len; i++) {
			sb.append(StringUtils.upperCaseFirstLetter(fields[i]));
		}
		return sb.toString();
	}

	/**
	 * 处理字段，转换为 Java类型
	 *
	 * @param type
	 * @return
	 */
	private static String processJavaType(String type) {

		if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPES, type)) {
			return "Integer";
		} else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, type)) {
			return "Long";
		} else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)) {
			return "String";
		} else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
			return "Date";
		} else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
			return "BigDecimal";
		} else {
			throw new RuntimeException("无法识别的类型" + type);
		}
	}
}
