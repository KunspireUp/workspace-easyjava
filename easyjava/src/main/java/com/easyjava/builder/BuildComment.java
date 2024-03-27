package com.easyjava.builder;

import com.easyjava.bean.Constants;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: 生成类注解
 * @Author: KunSpireUp
 * @Date: 8/3/2024 下午11:22
 */
public class BuildComment {

	public static void creatClassComment(BufferedWriter bw, String className) {
		try {
			bw.write("/**\n");
			bw.write(" * @Description: " + className + "\n");
			bw.write(" * @Author: " + Constants.COMMENT_AUTHOR + "\n");
			bw.write(" * @Date: " + DateUtils.format(new Date(), DateUtils.YYYY_A_MM_A_DD_HH_MM_SS) + "\n");
			bw.write(" */\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void creatFieldComment(BufferedWriter bw, String fieldComment) {
		try {
			bw.write("	/**\n");
			bw.write(" 	 * " + fieldComment + "\n");
			bw.write(" 	 */\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void creatMethodComment() {
		
	}
}
