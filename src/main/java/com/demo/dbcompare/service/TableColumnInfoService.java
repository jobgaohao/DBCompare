package com.demo.dbcompare.service;

import com.demo.dbcompare.dto.CompareTast;

public interface TableColumnInfoService {

	/**
	 * 根据入参的数据库连接信息查询表结构信息
	 * @param compareTast
	 * @return
	 * @author: jie.deng
	 */
	CompareTast seizeTableColumnInfo(CompareTast compareTast);

}
