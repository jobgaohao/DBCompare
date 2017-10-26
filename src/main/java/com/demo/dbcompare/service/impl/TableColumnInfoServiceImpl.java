package com.demo.dbcompare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.demo.dbcompare.dto.CompareTast;
import com.demo.dbcompare.dto.ConnConf;
import com.demo.dbcompare.factory.MapperFactoty;
import com.demo.dbcompare.mapper.TableColumnInfoMapper;
import com.demo.dbcompare.model.TableColumnInfo;
import com.demo.dbcompare.service.TableColumnInfoService;

@Service
public class TableColumnInfoServiceImpl implements TableColumnInfoService {
	
	public static final int PER_TABLE_COUNT = 80;	//每批次传入80张表名查询列信息
	
	private static final Map<ConnConf,TableColumnInfoMapper> mapperMap = new ConcurrentHashMap<>();
	
	@Override
	public CompareTast seizeTableColumnInfo(CompareTast compareTast) {
		//入参处理，填充driverClass等
		if (StringUtils.isEmpty(compareTast.getDriverLeft())
				|| StringUtils.isEmpty(compareTast.getDriverRight())) {
			compareTast.paramPostProcessor();
		}
		
		ConnConf connConfLeft = new ConnConf(
				compareTast.getDriverLeft(),
				compareTast.getUrlLeft(),
				compareTast.getUsernameLeft(),
				compareTast.getPasswordLeft());
		TableColumnInfoMapper tableColumnInfoMapperLeft = mapperMap.get(connConfLeft);
		if (null == tableColumnInfoMapperLeft) {
			//根据数据库连接信息获取mapper
			tableColumnInfoMapperLeft = MapperFactoty.getMapper(connConfLeft, TableColumnInfoMapper.class);
			if (null != tableColumnInfoMapperLeft) {
				mapperMap.put(connConfLeft, tableColumnInfoMapperLeft);
			}
		}
		
		ConnConf connConfRight = new ConnConf(
				compareTast.getDriverRight(),
				compareTast.getUrlRight(),
				compareTast.getUsernameRight(),
				compareTast.getPasswordRight());
		TableColumnInfoMapper tableColumnInfoMapperRight = mapperMap.get(connConfRight);
		if (null == tableColumnInfoMapperRight) {
			//根据数据库连接信息获取mapper
			tableColumnInfoMapperRight = MapperFactoty.getMapper(connConfRight, TableColumnInfoMapper.class);
			if (null != tableColumnInfoMapperRight) {
				mapperMap.put(connConfRight, tableColumnInfoMapperRight);
			}
		}
		
		//如果页面有传要比较的表名，则根据表名查询列信息，进一步比较列信息差异
		if (CollectionUtils.isNotEmpty(compareTast.getTableNameListLeft())
				|| CollectionUtils.isNotEmpty(compareTast.getTableNameListRight())) {
			List<TableColumnInfo> tableColumnInfoLeftList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(compareTast.getTableNameListLeft())) {
				tableColumnInfoLeftList = tableColumnInfoMapperLeft.findByTableNameList(compareTast.getTableNameListLeft(), compareTast.getDbNameLeft());
			}
			
			List<TableColumnInfo> tableColumnInfoRightList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(compareTast.getTableNameListRight())) {
				tableColumnInfoRightList = tableColumnInfoMapperRight.findByTableNameList(compareTast.getTableNameListRight(), compareTast.getDbNameRight());
			}
			
			compareTast.setTableColumnInfoLeft(tableColumnInfoLeftList);
			compareTast.setTableColumnInfoRight(tableColumnInfoRightList);
			compareTast.updateTableNameDiff(tableColumnInfoLeftList, tableColumnInfoRightList);
			return compareTast;
		}
		
		//如果页面没有传待比较的表名列表，则根据数据库名查询所有表，比较表信息差异，然后表名相同的进一步比较列信息差异
		List<String> tableNameLeftList = new ArrayList<>();
		if (CollectionUtils.isEmpty(compareTast.getTableNameListLeft())) {
			//没有传入表名列表，默认比较连接下所有的表
			tableNameLeftList.addAll(tableColumnInfoMapperLeft.findTables(compareTast.getDbNameLeft()));
		}
		List<String> tableNameRightList = new ArrayList<>();
		if (CollectionUtils.isEmpty(compareTast.getTableNameListRight())) {
			//没有传入表名列表，默认比较连接下所有的表
			tableNameRightList.addAll(tableColumnInfoMapperRight.findTables(compareTast.getDbNameRight()));
		}
		
		//比较表信息差异
		compareTast.genTableNameDiff(tableNameLeftList, tableNameRightList);
		
		if (CollectionUtils.isEmpty(compareTast.getTableColumnInfoLeft())) {
			List<String> tableNameList = compareTast.getTableNameListLeft();
			int fromIndex = 0, toIndex = 0;
			int size = tableNameList.size(); 
			while (fromIndex < size) {
				toIndex = Math.min(toIndex + PER_TABLE_COUNT, size);
				compareTast.getTableColumnInfoLeft().addAll(tableColumnInfoMapperLeft.findByTableNameList(tableNameList.subList(fromIndex, toIndex), compareTast.getDbNameLeft()));
				fromIndex = toIndex;
			}			
		}
		if (CollectionUtils.isEmpty(compareTast.getTableColumnInfoRight())) {
			List<String> tableNameList = compareTast.getTableNameListRight();
			int fromIndex = 0, toIndex = 0;
			int size = tableNameList.size(); 
			while (fromIndex < size) {
				toIndex = Math.min(toIndex + PER_TABLE_COUNT, size);
				compareTast.getTableColumnInfoRight().addAll(tableColumnInfoMapperRight.findByTableNameList(tableNameList.subList(fromIndex, toIndex), compareTast.getDbNameRight()));
				fromIndex = toIndex;
			}
		}
		
		return compareTast;
	}

}
