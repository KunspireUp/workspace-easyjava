package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.builder.*;

import java.util.List;

/**
 * @Description: 启动类
 * @Author: KunSpireUp
 * @Date: 6/3/2024 下午9:24
 */
public class RunApplication {
	public static void main(String[] args) {
		List<TableInfo> tableInfoList = BuildTable.getTable();

		BuildBase.execute();

		for (TableInfo tableInfo : tableInfoList) {
			BuildPo.execute(tableInfo);

			BuildQuery.execute(tableInfo);

			BuildMapper.execute(tableInfo);

			BuildMapperXml.execute(tableInfo);

			BuildService.execute(tableInfo);

			BuildServiceImpl.execute(tableInfo);

			BuildController.execute(tableInfo);
		}

	}
}
