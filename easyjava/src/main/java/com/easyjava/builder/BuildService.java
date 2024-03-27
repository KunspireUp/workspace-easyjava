package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Description: 建立 Service 类
 * @Author: KunSpireUp
 * @Date: 3/25/2024 9:39 PM
 */
public class BuildService {

	private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_SERVICE);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String className = tableInfo.getBeanName() + "Service";
		File poFile = new File(folder, className + ".java");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/** 生成包名 */
			bw.write("package " + Constants.PACKAGE_SERVICE + ";");
			bw.newLine();
			bw.newLine();

			/*			*//** 引入序列化包 *//*
			bw.write("import java.io.Serializable;");
			bw.newLine();

			*//** 如果存在日期类型 Date or DateTime，则导入日期类型的包 *//*
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
			}*/
			bw.newLine();
			bw.write("import java.util.List;\n");
			bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
			bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
			bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";\n");

			BuildComment.creatClassComment(bw, tableInfo.getComment() + " Service");
			bw.write("public interface " + className + "{\n\n");

			BuildComment.creatFieldComment(bw, "根据条件查询列表");
			bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query);\n\n");

			BuildComment.creatFieldComment(bw, "根据条件查询数量");
			bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " query);\n\n");

			BuildComment.creatFieldComment(bw, "分页查询");
			bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query);\n\n");

			BuildComment.creatFieldComment(bw, "新增");
			bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);\n\n");

			BuildComment.creatFieldComment(bw, "批量新增");
			bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);\n\n");

			BuildComment.creatFieldComment(bw, "批量新增或修改");
			bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);\n");

			for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
				List<FieldInfo> keyFieldInfoList = entry.getValue();

				Integer index = 0;
				StringBuilder methodName = new StringBuilder();

				StringBuilder methodParam = new StringBuilder();

				for (FieldInfo fieldInfo : keyFieldInfoList) {
					index++;
					methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
					if (index < keyFieldInfoList.size()) {
						methodName.append("And");
					}

					methodParam.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
					if (index < keyFieldInfoList.size()) {
						methodParam.append(", ");
					}
				}
				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 查询");
				bw.write("\t" + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ");");
				bw.newLine();

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 更新");
				bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParam + "); ");
				bw.newLine();

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 删除");
				bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ");");
				bw.newLine();
			}

			bw.write("}");


			bw.flush();
		} catch (Exception e) {
			logger.info("创建 service 失败");
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
