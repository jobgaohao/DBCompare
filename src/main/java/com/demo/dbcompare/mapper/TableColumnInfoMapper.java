package com.demo.dbcompare.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.demo.dbcompare.model.TableColumnInfo;

public interface TableColumnInfoMapper {
    
	/**
	 * 根据表名列表查询数据库列结构信息
	 * @param tableNameList
	 * @return
	 * @author: jie.deng
	 */
    List<TableColumnInfo> findByTableNameList(@Param("list")List<String> tableNameList, @Param("dbName")String dbName);
    
    /**
     * 根据数据库或者用户名查询表名
     * @param dbName
     * @return
     * @author: jie.deng
     */
    List<String> findTables(@Param("dbName")String dbName);    
}