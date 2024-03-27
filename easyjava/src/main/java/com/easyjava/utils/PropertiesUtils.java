package com.easyjava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 读取配置文件
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午9:00
 */
public class PropertiesUtils {
	private static Properties props = new Properties();
	private static Map<String, String> PROPER_MAP = new ConcurrentHashMap();

	static {
		InputStream is = null;
		try{
			is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
			props.load(new InputStreamReader(is,"utf-8") {
				@Override
				public int read() throws IOException {
					return 0;
				}
			});

			Iterator<Object> iterator = props.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				PROPER_MAP.put(key, props.getProperty(key));
			}
		}catch (Exception e){

		}finally {
			if(is != null){
				try{
					is.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static String getString(String key) {
		return PROPER_MAP.get(key);
	}

	public static void main(String[] args) {
		System.out.println(getString("db.driver.name"));
	}







}
