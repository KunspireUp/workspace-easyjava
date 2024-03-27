package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Description: 生成 PO
 * @Author: KunSpireUp
 * @Date: 7/3/2024 下午5:07
 */
public class BuildPo {

	private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_PO);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File poFile = new File(folder, tableInfo.getBeanName() + ".java");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/** 生成包名 */
			bw.write("package " + Constants.PACKAGE_PO + ";");
			bw.newLine();
			bw.newLine();

			/** 引入序列化包 */
			bw.write("import java.io.Serializable;");
			bw.newLine();

			/** 如果存在日期类型 Date or DateTime，则导入日期类型的包 */
			if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
				bw.newLine();
				bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
				bw.newLine();
				bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
				bw.newLine();
				bw.write("import java.util.Date;");
				bw.newLine();
				bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
				bw.newLine();
				bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS);
			}

			/** 是否存在忽略属性 */
			Boolean haveIgnoreBean = false;
			/** 如果存在就不导入包 */
			for (FieldInfo field : tableInfo.getFieldList()) {
				if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), field.getPropertyName())) {
					haveIgnoreBean = true;
					break;
				}
			}
			/** 如果存在就导入包 */
			if (haveIgnoreBean) {
				/** 导入 @JsonIgnore 所属的包 */
				bw.write("\n" + String.format(Constants.IGNORE_BEAN_TOJSON_CLASS) + "\n");
			}


			/** 如果存在 BigDecimal 类型，则引入 BigDecimal 类型的包 */
			if (tableInfo.getHaveBigDecimal()) {
				bw.write("\n" + "import java.math.BigDecimal;");
			}
			bw.newLine();
			bw.newLine();

			/** 生成类顶部的注解【描述、作者、功能】 */
			BuildComment.creatClassComment(bw, tableInfo.getComment());

			/** 生成主类和序列化接口 */
			bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
			bw.newLine();

			/** 遍历生成注解 */
			for (FieldInfo field : tableInfo.getFieldList()) {
				BuildComment.creatFieldComment(bw, field.getComment());

				/** 存在 SQL_DATE_TIME_TYPES 类型 */
				if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
					/** 生成时间格式序列化 */
					bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS) + "\n");

					/** 生成时间格式反序列化 */
					bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS) + "\n");
				}

				/** 存在 SQL_DATE_TYPES 类型 */
				if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
					/** 生成时间格式序列化 */
					bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD) + "\n");

					/** 生成时间格式反序列化 */
					bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD) + "\n");
				}

				/** 存在忽略表的字段 */
				if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), field.getPropertyName())) {
					/** 生成忽略字段注解 @JsonIgnore */
					bw.write("\t" + String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESSION) + "\n");
				}

				bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
				bw.newLine();
				bw.newLine();
			}

			/** 生成 PO 的 set 和 get 方法 */
			for (FieldInfo field : tableInfo.getFieldList()) {
				String tempField = StringUtils.upperCaseFirstLetter(field.getPropertyName());
				// set
				bw.write("\n\tpublic void set" + tempField + "(" + field.getJavaType() + " " + field.getPropertyName() + ") {");
				bw.newLine();
				bw.write("\t\tthis." + field.getPropertyName() + " = " + field.getPropertyName() + ";");
				bw.newLine();
				bw.write("\t}" + "\n");

				// get
				bw.write("\n\tpublic " + field.getJavaType() + " get" + tempField + "() {");
				bw.newLine();
				bw.write("\t\treturn " + field.getPropertyName() + ";");
				bw.newLine();
				bw.write("\t}" + "\n");
			}

			/** 重写 toString 方法 */
			StringBuffer toString = new StringBuffer();
			Integer index = 0;
			for (FieldInfo field : tableInfo.getFieldList()) {
				index++;

				String properName = field.getPropertyName();
				if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
					properName = "DateUtils.format(" + properName + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
				} else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
					properName = "DateUtils.format(" + properName + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
				}

				/** 不换行格式 */
				/*toString.append(field.getComment() + ":\" + (" + field.getPropertyName()
						+ " == null ? \"空\" : " + properName + ")");
				if (index < tableInfo.getFieldList().size()) {
					toString.append(" + ").append("\",");
				}*/

				toString.append(field.getComment() + ":\" + (" + field.getPropertyName()
						+ " == null ? \"空\" : " + properName + ")");
				if (index < tableInfo.getFieldList().size()) {
					toString.append(" + ").append("\"," + "\" + \n\t\t\t\t\"");

				}
			}
			String toStringStr = toString.toString();
			toStringStr = "\"" + toStringStr;
			//System.out.println(toStringStr);
			bw.write("\t@Override\n");
			bw.write("\tpublic String toString() {\n");
			bw.write("\t\treturn " + toStringStr + ";\n");
			bw.write("\t\t}\n");

			bw.write("}");
			bw.flush();
		} catch (Exception e) {
			logger.info("创建 po 失败");
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
