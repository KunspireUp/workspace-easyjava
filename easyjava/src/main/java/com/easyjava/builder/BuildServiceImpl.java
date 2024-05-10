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
public class BuildServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

	public static void execute(TableInfo tableInfo) {
		File folder = new File(Constants.PATH_SERVICE_IMPL);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String interfaceName = tableInfo.getBeanName() + "Service";
		String className = tableInfo.getBeanName() + "ServiceImpl";
		String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
		String mappserBeanName = StringUtils.lowerCaseFirstLetter(mapperName);
		File poFile = new File(folder, className + ".java");
		OutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(poFile);
			outWriter = new OutputStreamWriter(out, "utf8");
			bw = new BufferedWriter(outWriter);

			/** 生成包名 */
			bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
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
			bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;\n");
			bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;\n");
			bw.write("import " + Constants.PACKAGE_MAPPERS + "." + mapperName + ";\n");
			bw.write("import " + Constants.PACKAGE_SERVICE + "." + interfaceName + ";\n");
			bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;\n");
			bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";\n");
			bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";\n");
			bw.write("import org.springframework.stereotype.Service;\n");
			bw.write("import javax.annotation.Resource;\n");

			BuildComment.creatClassComment(bw, tableInfo.getComment() + " 业务接口实现");

			bw.write("@Service(\"" + mapperName  + "\")\n");
			bw.write("public class " + className + " implements " + interfaceName + "{\n\n");

			bw.write("\t@Resource\n");
			bw.write("\tprivate " + mapperName + "<" + tableInfo.getBeanName() + ", " + tableInfo.getBeanParamName() + "> " + mappserBeanName + ";\n\n");

			BuildComment.creatFieldComment(bw, "根据条件查询列表");
			bw.write("\t@Override\n");
			bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query) {\n");
			bw.write("\t\treturn this." + mappserBeanName + ".selectList(query);");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "根据条件查询数量");
			bw.write("\t@Override\n");
			bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " query) {\n");
			bw.write("\t\treturn this." + mappserBeanName + ".selectCount(query);");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "分页查询");
			bw.write("\t@Override\n");
			bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query) {\n");
			bw.write("\t\tInteger count = this.findCountByParam(query);\n");
			bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();\n");
			bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(), count, pageSize);\n");
			bw.write("\t\tquery.setSimplePage(page);\n");
			bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);\n");
			bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName() + "> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);\n");
			bw.write("\t\treturn result;\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "新增");
			bw.write("\t@Override\n");
			bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {\n");
			bw.write("\t\treturn this." + mappserBeanName + ".insert(bean);\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "批量新增");
			bw.write("\t@Override\n");
			bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {\n");
			bw.write("\t\tif ((listBean == null) || listBean.isEmpty()) {\n");
			bw.write("\t\t\treturn 0;\n");
			bw.write("\t\t}\n");
			bw.write("\t\t\treturn this." + mappserBeanName + ".insertBatch(listBean);\n");
			bw.write("\t}\n\n");

			BuildComment.creatFieldComment(bw, "批量新增或修改");
			bw.write("\t@Override\n");
			bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {\n");
			bw.write("\t\tif ((listBean == null) || listBean.isEmpty()) {\n");
			bw.write("\t\t\treturn 0;\n");
			bw.write("\t\t}\n");
			bw.write("\t\t\treturn this." + mappserBeanName + ".insertOrUpdateBatch(listBean);\n");
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
				bw.write("\t@Override\n");
				bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ") {\n");
				bw.write("\t\treturn this." + mappserBeanName + ".selectBy" + methodName + "(" + paramsBuilder + ");");
				bw.write("}\n");

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 更新");
				bw.write("\t@Override\n");
				bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParam + ") {\n");
				bw.write("\t\treturn this." + mappserBeanName + ".updateBy" + methodName + "(bean, " + paramsBuilder + ");");
				bw.write("}\n");

				bw.newLine();
				BuildComment.creatFieldComment(bw, "根据 " + methodName + " 删除");
				bw.write("\t@Override\n");
				bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParam + ") {\n");
				bw.write("\t\treturn this." + mappserBeanName + ".deleteBy" + methodName + "(" + paramsBuilder + ");");
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
