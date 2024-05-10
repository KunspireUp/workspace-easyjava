package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KunSpireUp
 * @Date: 3/26/2024 3:50 PM
 */
public class BuildController {

	private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_CONTROLLER);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String className = tableInfo.getBeanName() + "Controller";
		String serviceName = tableInfo.getBeanName() + "Service";
		String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
		File poFile = new File(folder, className + ".java");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/** 生成包名 */
			bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
			bw.newLine();
			bw.newLine();

			/** 引入序列化包 *//*
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
			bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";\n");
			bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;\n");
			bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
			bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";\n");
			bw.write("import org.springframework.web.bind.annotation.RequestBody;\n");
			bw.write("import org.springframework.web.bind.annotation.RestController;\n");
			bw.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
			bw.write("import javax.annotation.Resource;\n");

			BuildComment.creatClassComment(bw, tableInfo.getComment() + " Controller");

			bw.write("@RestController\n");
			bw.write("@RequestMapping(\"/" + StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName()) + "\")\n");
			bw.write("public class " + className + " extends ABaseController{\n\n");

			bw.write("\t@Resource\n");
			bw.write("\tprivate " + serviceName + " " + serviceBeanName + ";\n\n");

			bw.write("\t@RequestMapping(\"/loadDataList\")\n");
			bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanParamName() + " query) {\n");
			bw.write("\t\treturn getSuccessResponseVO(" + serviceBeanName + ".findListByPage(query));\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "新增");
			bw.write("\t@RequestMapping(\"/add\")\n");
			bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean) {\n");
			bw.write("\t\tInteger result = this." + serviceBeanName + ".add(bean);\n");
			bw.write("\t\treturn getSuccessResponseVO(null);\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "批量新增");
			bw.write("\t@RequestMapping(\"/addBatch\")\n");
			bw.write("\tpublic ResponseVO addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
			bw.write("\t\tthis." + serviceBeanName + ".addBatch(listBean);\n");
			bw.write("\t\treturn getSuccessResponseVO(null);\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "批量新增或修改");
			bw.write("\t@RequestMapping(\"/addOrUpdateBatch\")\n");
			bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {\n");
			bw.write("\t\tthis." + serviceBeanName + ".addOrUpdateBatch(listBean);\n");
			bw.write("\t\treturn getSuccessResponseVO(null);\n");
			bw.write("\t}\n");

			for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
				List<FieldInfo> keyFieldInfoList = entry.getValue();

				Integer index = 0;
				StringBuilder methodName = new StringBuilder();

				StringBuilder methodParam = new StringBuilder();

				StringBuilder paramsBuilder = new StringBuilder();

				for (FieldInfo fieldInfo : keyFieldInfoList) {
					index++;
					methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
					if (index < keyFieldInfoList.size()) {
						methodName.append("And");
					}

					methodParam.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
					paramsBuilder.append(fieldInfo.getPropertyName());
					if (index < keyFieldInfoList.size()) {
						methodParam.append(", ");
						paramsBuilder.append(", ");
					}
				}
				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 查询");
				bw.write("\t@RequestMapping(\"/get" + tableInfo.getBeanName() + "By" + methodName + "\")\n");
				bw.write("\tpublic ResponseVO get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ") {\n");
				bw.write("\t\treturn getSuccessResponseVO(" + serviceBeanName + ".get" + tableInfo.getBeanName() + "By" + methodName + "(" + paramsBuilder + "));");
				bw.write("}\n");

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 更新");
				bw.write("\t@RequestMapping(\"/update" + tableInfo.getBeanName() + "By" + methodName + "\")\n");
				bw.write("\tpublic ResponseVO update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParam + ") {\n");
				bw.write("\t\tthis." + serviceBeanName + ".update" + tableInfo.getBeanName() + "By" + methodName + "(bean, " + paramsBuilder + ");\n");
				bw.write("\t\treturn getSuccessResponseVO(null);\n");
				bw.write("}\n");

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 删除");
				bw.write("\t@RequestMapping(\"/delete" + tableInfo.getBeanName() + "By" + methodName + "\")\n");
				bw.write("\tpublic ResponseVO delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ") {\n");
				bw.write("\t\tthis." + serviceBeanName + ".delete" + tableInfo.getBeanName() + "By" + methodName + "(" + paramsBuilder + ");\n");
				bw.write("\t\treturn getSuccessResponseVO(null);\n");
				bw.write("}\n");
			}

			bw.write("}");


			bw.flush();
		} catch (Exception e) {
			logger.info("创建 serviceImpl 失败");
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
