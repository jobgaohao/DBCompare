package com.demo.dbcompare.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import com.demo.dbcompare.model.TableColumnInfo;

public class DbCompareDiff {
	//左连接名
	private String connNameLeft;

	//右连接名
	private String connNameRight;
	
	//左表名
	private String tableNameLeft;

	//列名:比较的是同名列的结构信息
	private String columnName;
	
	//右表名
	private String tableNameRight;
	
	//列表的第一个元素为左连接的字段结构信息，第二个元素为右连接的字段结构信息，如果字段缺失为空的TableColumnInfo对象
	private List<TableColumnInfo> diffColumnlist;

	public static List<DbCompareDiff> genDiff(CompareTast compareTast) {
		List<DbCompareDiff> dbCompareDiffList = new ArrayList<>();
		
		List<TableColumnInfo> tableColumnInfoListLeft = compareTast.getTableColumnInfoLeft();
		List<TableColumnInfo> tableColumnInfoListRight = compareTast.getTableColumnInfoRight();
		
		Map<String, TableRelation> tableRelationLeftMap = compareTast.getTableRelationLeftMap();
		Map<String, TableRelation> tableRelationRightMap = compareTast.getTableRelationRightMap();
		
		Map<String, TableColumnInfo> tableColumnInfoMapLeft = new TreeMap<>();
		if (CollectionUtils.isNotEmpty(tableColumnInfoListLeft)) {
			for (TableColumnInfo tableColumnInfo : tableColumnInfoListLeft) {
				TableRelation tableRelation = tableRelationLeftMap.get(tableColumnInfo.getTableName().toUpperCase());
				if (null != tableRelation) {
					tableColumnInfoMapLeft.put(tableRelation.getTableNameLeft()+"."+tableRelation.getTableNameRight()+"."+tableColumnInfo.getColumnName(), tableColumnInfo);
				}
			};
		}
		Map<String, TableColumnInfo> tableColumnInfoMapRight = new TreeMap<>();
		if (CollectionUtils.isNotEmpty(tableColumnInfoListRight)) {
			for (TableColumnInfo tableColumnInfo : tableColumnInfoListRight) {
				TableRelation tableRelation = tableRelationRightMap.get(tableColumnInfo.getTableName().toUpperCase());
				if (null != tableRelation) {
					tableColumnInfoMapRight.put(tableRelation.getTableNameLeft()+"."+tableRelation.getTableNameRight()+"."+tableColumnInfo.getColumnName(), tableColumnInfo);
				}				
			};
		}
		
		//左连接有表信息，右连接表缺失的数据，每张表产生一条差异记录
		ArrayList<TableColumnInfo> defaultDiffColumnlist = new ArrayList<>(2);
		defaultDiffColumnlist.add(new TableColumnInfo());
		defaultDiffColumnlist.add(new TableColumnInfo());
		for (String tableName : compareTast.getTableNameRightMissedSet()) {
			DbCompareDiff dbCompareDiff = new DbCompareDiff();
			dbCompareDiff.setConnNameLeft(compareTast.getConnNameLeft());
			dbCompareDiff.setConnNameRight(compareTast.getConnNameRight());
			dbCompareDiff.setColumnName("");
			dbCompareDiff.setDiffColumnlist(defaultDiffColumnlist);
			dbCompareDiff.setTableNameLeft(tableName);
			dbCompareDiff.setTableNameRight("");
			dbCompareDiffList.add(dbCompareDiff);			
		}
		//右连接有表信息，左连接表缺失的数据，每张表产生一条差异记录
		for (String tableName : compareTast.getTableNameLeftMissedSet()) {
			DbCompareDiff dbCompareDiff = new DbCompareDiff();
			dbCompareDiff.setConnNameLeft(compareTast.getConnNameLeft());
			dbCompareDiff.setConnNameRight(compareTast.getConnNameRight());
			dbCompareDiff.setColumnName("");
			dbCompareDiff.setDiffColumnlist(defaultDiffColumnlist);
			dbCompareDiff.setTableNameLeft("");
			dbCompareDiff.setTableNameRight(tableName);
			dbCompareDiffList.add(dbCompareDiff);			
		}
		//左右连接都有表信息，但是字段结构存在差异的，每个字段产生一条差异记录
		for (String columnName : getDistinctColumnNameList(tableColumnInfoMapLeft, tableColumnInfoMapRight)) {
			TableColumnInfo tableColumnInfoLeft = tableColumnInfoMapLeft.get(columnName);
			TableColumnInfo tableColumnInfoRight = tableColumnInfoMapRight.get(columnName);
			if (tableColumnInfoLeft == null && tableColumnInfoRight == null) {
				continue;
			}
			if (!Objects.equals(tableColumnInfoLeft, tableColumnInfoRight)) {
				String[] strs = columnName.split("\\.");
				Assert.isTrue(strs.length == 3);
				DbCompareDiff dbCompareDiff = new DbCompareDiff();
				dbCompareDiff.setConnNameLeft(compareTast.getConnNameLeft());
				dbCompareDiff.setConnNameRight(compareTast.getConnNameRight());
				dbCompareDiff.setTableNameLeft(strs[0]);
				dbCompareDiff.setTableNameRight(strs[1]);
				dbCompareDiff.setColumnName(strs[2]);
				List<TableColumnInfo> list = new ArrayList<>(2);
				list.add(tableColumnInfoLeft == null ? new TableColumnInfo() : tableColumnInfoLeft);
				list.add(tableColumnInfoRight == null ? new TableColumnInfo() : tableColumnInfoRight);
				dbCompareDiff.setDiffColumnlist(list);
				dbCompareDiffList.add(dbCompareDiff);
			}
		}

		return dbCompareDiffList;
	}
	
	private static List<String> getDistinctColumnNameList(
			Map<String, TableColumnInfo> tableColumnInfoMapLeft, 
			Map<String, TableColumnInfo> tableColumnInfoMapRight) {
		List<String> distinctColumnNameList = new ArrayList<>();
		distinctColumnNameList.addAll(tableColumnInfoMapLeft.keySet());
		for (String columnName : tableColumnInfoMapRight.keySet()) {
			if (!distinctColumnNameList.contains(columnName)) {
				distinctColumnNameList.add(columnName);
			}
		}
		return distinctColumnNameList;
	}

	public String getConnNameLeft() {
		return connNameLeft;
	}

	public void setConnNameLeft(String connNameLeft) {
		this.connNameLeft = connNameLeft;
	}

	public String getConnNameRight() {
		return connNameRight;
	}

	public void setConnNameRight(String connNameRight) {
		this.connNameRight = connNameRight;
	}

	public String getTableNameLeft() {
		return tableNameLeft;
	}

	public void setTableNameLeft(String tableNameLeft) {
		this.tableNameLeft = tableNameLeft;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableNameRight() {
		return tableNameRight;
	}

	public void setTableNameRight(String tableNameRight) {
		this.tableNameRight = tableNameRight;
	}

	public List<TableColumnInfo> getDiffColumnlist() {
		return diffColumnlist;
	}

	public void setDiffColumnlist(List<TableColumnInfo> diffColumnlist) {
		this.diffColumnlist = diffColumnlist;
	}

}
