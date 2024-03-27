package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 生成 Query
 * @Author: KunSpireUp
 * @Date: 7/3/2024 下午5:07
 */
public class BuildQuery {

	private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_QUERY);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;

		File poFile = new File(folder, className + ".java");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/** 生成包名 */
			bw.write("package " + Constants.PACKAGE_QUERY + ";");
			bw.newLine();
			bw.newLine();

			/** 如果存在日期类型 Date or DateTime，则导入日期类型的包 */
			if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
				bw.write("import java.util.Date;");
				bw.newLine();
			}

			/** 如果存在 BigDecimal 类型，则引入 BigDecimal 类型的包 */
			if (tableInfo.getHaveBigDecimal()) {
				bw.write("\n" + "import java.math.BigDecimal;");
			}
			bw.newLine();
			bw.newLine();

			/** 生成类顶部的注解【描述、作者、功能】 */
			BuildComment.creatClassComment(bw, tableInfo.getComment());

			/** 生成主类 */
			bw.write("public class " + className + " extends BaseQuery {");
			bw.newLine();

			List<FieldInfo> extendList = new ArrayList();

			/** 遍历生成注解 */
			for (FieldInfo field : tableInfo.getFieldList()) {

				/** 生成 顶部注解 */
				BuildComment.creatFieldComment(bw, field.getComment() + " 查询对象");

				bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
				bw.newLine();
				bw.newLine();

				/** String 类型的参数 + Fuzzy */
				if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, field.getSqlType())) {
					String propertyName = field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;
					bw.write("\tprivate " + field.getJavaType() + " " + propertyName + ";");
					bw.newLine();
					bw.newLine();
				}

				/** 日期类型的参数起止 */
				if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
					bw.write("\tprivate String " + field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
					bw.newLine();
					bw.write("\tprivate String " + field.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
					bw.newLine();
				}
			}
			buildGetSet(bw, tableInfo.getFieldList());
			buildGetSet(bw, tableInfo.getFieldExtendList());
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

	private static void buildGetSet(BufferedWriter bw, List<FieldInfo> fieldInfoList) throws IOException {
		/** 生成 PO 的 set 和 get 方法 */
		for (FieldInfo field : fieldInfoList) {
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
	}
}
