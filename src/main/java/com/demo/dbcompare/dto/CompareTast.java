package com.demo.dbcompare.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import com.demo.dbcompare.model.TableColumnInfo;

public class CompareTast {
	/**
	 * 入参部分
	 */
	// 入参connNameLeft
	private String connNameLeft;

	// 根据入参urlLeft计算得到
	private String dbNameLeft;

	// 入参tableNameListLeft，可不传
	private List<String> tableNameListLeft;

	// 入参usernameLeft
	private String usernameLeft;

	// 入参passwordLeft
	private String passwordLeft;

	// 根据入参urlLeft计算得到
	private String driverLeft;

	// 入参urlLeft
	private String urlLeft;

	// 入参connNameRight
	private String connNameRight;

	// 根据入参urlRight计算得到
	private String dbNameRight;

	// 入参tableNameListRight，可不传
	private List<String> tableNameListRight;

	// 入参usernameRight
	private String usernameRight;

	// 入参passwordRight
	private String passwordRight;

	// 根据入参urlRight计算得到
	private String driverRight;

	// 入参urlLeft
	private String urlRight;

	/**
	 * 数据库查询到的列结构信息
	 */
	// 查询数据库得到的左连接的列信息列表
	private List<TableColumnInfo> tableColumnInfoLeft = new ArrayList<>();

	// 查询数据库得到的右连接的列信息列表
	private List<TableColumnInfo> tableColumnInfoRight = new ArrayList<>();
	
	/**
	 * 左右表对应关系信息
	 */
	//根据页面输入的表名，确定的左右表对应关系列表
	private List<TableRelation> tableRelationList = new ArrayList<>();
	
	//key为tableName
	private Map<String, TableRelation> tableRelationLeftMap = new HashMap<>();
	
	//key为tableName
	private Map<String, TableRelation> tableRelationRightMap = new HashMap<>();
	
	/**
	 * 表结构差异信息
	 */
	private Set<String> tableNameLeftMissedSet = new TreeSet<>();
	private Set<String> tableNameRightMissedSet = new TreeSet<>();
	
	/**
	 * 入参后处理:
	 * 1.driver根据url计算得到
	 * 2.dbName根据url和username计算得到
	 * 3.tableName转为大写格式
	 * 4.建立表对应关系
	 * @param compareTast
	 * @author: jie.deng
	 */
	public void paramPostProcessor() {
		CompareTast compareTast = this;
		//1.driver根据url计算得到
		//2.dbName根据url和username计算得到
		String urlLeft = compareTast.getUrlLeft();
		if (urlLeft.toUpperCase().contains("ORACLE")) {
			compareTast.setDriverLeft("oracle.jdbc.driver.OracleDriver");
			compareTast.setDbNameLeft(compareTast.getUsernameLeft().toUpperCase());
		}else if (urlLeft.toUpperCase().contains("MYSQL")) {
			compareTast.setDriverLeft("com.mysql.jdbc.Driver");
			if(urlLeft.indexOf('?')==-1){
				compareTast.setDbNameLeft(urlLeft.substring(urlLeft.lastIndexOf('/')+1).toUpperCase());
			}else {
				compareTast.setDbNameLeft(urlLeft.substring(urlLeft.lastIndexOf('/')+1, urlLeft.indexOf('?')).toUpperCase());
			}
		}
		//3.tableName转为大写格式
		List<String> tableNameListLeft = compareTast.getTableNameListLeft();
		if (CollectionUtils.isNotEmpty(tableNameListLeft)) {
			List<String> list = new ArrayList<>();
			for (String str : tableNameListLeft) {
				if (null != str) {
					list.add(str.toUpperCase());
				}
			}
			compareTast.setTableNameListLeft(list);
		}
		
		//1.driver根据url计算得到
		//2.dbName根据url和username计算得到
		String urlRight = compareTast.getUrlRight();
		if (urlRight.toUpperCase().contains("ORACLE")) {
			compareTast.setDriverRight("oracle.jdbc.driver.OracleDriver");
			compareTast.setDbNameRight(compareTast.getUsernameRight().toUpperCase());
		}else if (urlRight.toUpperCase().contains("MYSQL")) {
			compareTast.setDriverRight("com.mysql.jdbc.Driver");
			if(urlRight.indexOf('?')==-1){
				compareTast.setDbNameRight(urlRight.substring(urlRight.lastIndexOf('/')+1).toUpperCase());
			}else {
				compareTast.setDbNameRight(urlRight.substring(urlRight.lastIndexOf('/')+1, urlRight.indexOf('?')).toUpperCase());
			}
		}
		//3.tableName转为大写格式
		List<String> tableNameListRight = compareTast.getTableNameListRight();
		if (CollectionUtils.isNotEmpty(tableNameListRight)) {
			List<String> list = new ArrayList<>();
			for (String str : tableNameListRight) {
				if (null != str) {
					list.add(str.toUpperCase());
				}
			}
			compareTast.setTableNameListRight(list);
		}else {
			//如果右连接表名列表没传只传了左连接表名列表，则右连接表名列表=左连接表名列表
			if (CollectionUtils.isNotEmpty(compareTast.getTableNameListLeft())) {
				compareTast.setTableNameListRight(compareTast.getTableNameListLeft());
			}			
		}
		
		//4.建立表对应关系
		genTableNameRelation(compareTast.getTableNameListLeft(), compareTast.getTableNameListRight());
	}

	/**
	 * 建立表对应关系
	 * @param tableNameListLeft
	 * @param tableNameListRight
	 * @author: jie.deng
	 * @time: 2017年4月14日 下午3:08:46
	 */
	public void genTableNameRelation(List<String> tableNameListLeft, List<String> tableNameListRight) {
		CompareTast compareTast = this;
		List<TableRelation> tableRelationList = new ArrayList<>();
		Map<String, TableRelation> tableRelationLeftMap = new HashMap<>();
		Map<String, TableRelation> tableRelationRightMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(tableNameListLeft)
				&& CollectionUtils.isNotEmpty(tableNameListRight)
				&& tableNameListLeft.size() == tableNameListRight.size()) {
			for (int i = 0; i < tableNameListLeft.size(); i++) {
				String tableNameLeft = tableNameListLeft.get(i);
				String tableNameRight = tableNameListRight.get(i);
				TableRelation tableRelation = new TableRelation(tableNameLeft, tableNameRight);
				tableRelationList.add(tableRelation);
				tableRelationLeftMap.put(tableNameLeft.toUpperCase(), tableRelation);
				tableRelationRightMap.put(tableNameRight.toUpperCase(), tableRelation);
			}
		}
		
		compareTast.setTableRelationList(tableRelationList);
		compareTast.setTableRelationLeftMap(tableRelationLeftMap);
		compareTast.setTableRelationRightMap(tableRelationRightMap);
	}
	
	public void updateTableNameRelation(Set<String> tableNameLeftMissedSet, Set<String> tableNameRightMissedSet) {
		CompareTast compareTast = this;
		List<TableRelation> tableRelationList = compareTast.getTableRelationList();
		for (Iterator<TableRelation> iterator = tableRelationList.iterator(); iterator.hasNext();) {
			TableRelation tableRelation = iterator.next();
			if (tableNameLeftMissedSet.contains(tableRelation.getTableNameRight())
					|| tableNameRightMissedSet.contains(tableRelation.getTableNameLeft())) {
				iterator.remove();
			}
		}
		Map<String, TableRelation> tableRelationLeftMap = new HashMap<>();
		Map<String, TableRelation> tableRelationRightMap = new HashMap<>();
		for (TableRelation tableRelation : tableRelationList) {
			tableRelationLeftMap.put(tableRelation.getTableNameLeft().toUpperCase(), tableRelation);
			tableRelationRightMap.put(tableRelation.getTableNameRight().toUpperCase(), tableRelation);			
		}
		compareTast.setTableRelationList(tableRelationList);
		compareTast.setTableRelationLeftMap(tableRelationLeftMap);
		compareTast.setTableRelationRightMap(tableRelationRightMap);
	}
	
	/**
	 * 处理左右连接表差异
	 * @param tableNameListLeft
	 * @param tableNameListRight
	 * @author: jie.deng
	 * @time: 2017年4月14日 下午3:10:19
	 */
	public void genTableNameDiff(List<String> tableNameListLeft,List<String> tableNameListRight) {
		CompareTast compareTast = this;
		
		Set<String> tableNameLeftSet = new TreeSet<>(tableNameListLeft);
		Set<String> tableNameRightSet = new TreeSet<>(tableNameListRight);
		
		Set<String> tableNameIntersectSet = new TreeSet<>();
		tableNameIntersectSet.addAll(tableNameLeftSet);
		tableNameIntersectSet.retainAll(tableNameRightSet);
		compareTast.setTableNameListLeft(new ArrayList<>(tableNameIntersectSet));
		compareTast.setTableNameListRight(new ArrayList<>(tableNameIntersectSet));
		
		Set<String> tableNameLeftMissedSet = new TreeSet<>();
		tableNameLeftMissedSet.addAll(tableNameRightSet);
		tableNameLeftMissedSet.removeAll(tableNameListLeft);
		compareTast.setTableNameLeftMissedSet(tableNameLeftMissedSet);
		
		Set<String> tableNameRightMissedSet = new TreeSet<>();
		tableNameRightMissedSet.addAll(tableNameListLeft);
		tableNameRightMissedSet.removeAll(tableNameRightSet);		
		compareTast.setTableNameRightMissedSet(tableNameRightMissedSet);
		if (CollectionUtils.isEmpty(compareTast.getTableRelationList())) {
			genTableNameRelation(compareTast.getTableNameListLeft(), compareTast.getTableNameListRight());
		}else {
			updateTableNameRelation(compareTast.getTableNameLeftMissedSet(), compareTast.getTableNameRightMissedSet());
		}
	}
	
	public void updateTableNameDiff(List<TableColumnInfo> tableColumnInfoLeft, List<TableColumnInfo> tableColumnInfoRight){
		Set<String> tableNameSetLeft = new TreeSet<>();
		Set<String> tableNameSetRight = new TreeSet<>();
		for (TableColumnInfo tableColumnInfo : tableColumnInfoLeft) {
			tableNameSetLeft.add(tableColumnInfo.getTableName());
		}
		for (TableColumnInfo tableColumnInfo : tableColumnInfoRight) {
			tableNameSetRight.add(tableColumnInfo.getTableName());
		}
		
		genTableNameDiff(new ArrayList<>(tableNameSetLeft), new ArrayList<>(tableNameSetRight));
		
	}
	
	public String getConnNameLeft() {
		return connNameLeft;
	}

	public void setConnNameLeft(String connNameLeft) {
		this.connNameLeft = connNameLeft;
	}

	public String getDbNameLeft() {
		return dbNameLeft;
	}

	public void setDbNameLeft(String dbNameLeft) {
		this.dbNameLeft = dbNameLeft;
	}

	public List<String> getTableNameListLeft() {
		return tableNameListLeft;
	}

	public void setTableNameListLeft(List<String> tableNameListLeft) {
		this.tableNameListLeft = tableNameListLeft;
	}

	public String getUsernameLeft() {
		return usernameLeft;
	}

	public void setUsernameLeft(String usernameLeft) {
		this.usernameLeft = usernameLeft;
	}

	public String getPasswordLeft() {
		return passwordLeft;
	}

	public void setPasswordLeft(String passwordLeft) {
		this.passwordLeft = passwordLeft;
	}

	public String getDriverLeft() {
		return driverLeft;
	}

	public void setDriverLeft(String driverLeft) {
		this.driverLeft = driverLeft;
	}

	public String getUrlLeft() {
		return urlLeft;
	}

	public void setUrlLeft(String urlLeft) {
		this.urlLeft = urlLeft;
	}

	public String getConnNameRight() {
		return connNameRight;
	}

	public void setConnNameRight(String connNameRight) {
		this.connNameRight = connNameRight;
	}

	public String getDbNameRight() {
		return dbNameRight;
	}

	public void setDbNameRight(String dbNameRight) {
		this.dbNameRight = dbNameRight;
	}

	public List<String> getTableNameListRight() {
		return tableNameListRight;
	}

	public void setTableNameListRight(List<String> tableNameListRight) {
		this.tableNameListRight = tableNameListRight;
	}

	public String getUsernameRight() {
		return usernameRight;
	}

	public void setUsernameRight(String usernameRight) {
		this.usernameRight = usernameRight;
	}

	public String getPasswordRight() {
		return passwordRight;
	}

	public void setPasswordRight(String passwordRight) {
		this.passwordRight = passwordRight;
	}

	public String getDriverRight() {
		return driverRight;
	}

	public void setDriverRight(String driverRight) {
		this.driverRight = driverRight;
	}

	public String getUrlRight() {
		return urlRight;
	}

	public void setUrlRight(String urlRight) {
		this.urlRight = urlRight;
	}

	public List<TableColumnInfo> getTableColumnInfoLeft() {
		return tableColumnInfoLeft;
	}

	public void setTableColumnInfoLeft(List<TableColumnInfo> tableColumnInfoLeft) {
		this.tableColumnInfoLeft = tableColumnInfoLeft;
	}

	public List<TableColumnInfo> getTableColumnInfoRight() {
		return tableColumnInfoRight;
	}

	public void setTableColumnInfoRight(
			List<TableColumnInfo> tableColumnInfoRight) {
		this.tableColumnInfoRight = tableColumnInfoRight;
	}

	public List<TableRelation> getTableRelationList() {
		return tableRelationList;
	}

	public void setTableRelationList(List<TableRelation> tableRelationList) {
		this.tableRelationList = tableRelationList;
	}

	public Map<String, TableRelation> getTableRelationLeftMap() {
		return tableRelationLeftMap;
	}

	public void setTableRelationLeftMap(
			Map<String, TableRelation> tableRelationLeftMap) {
		this.tableRelationLeftMap = tableRelationLeftMap;
	}

	public Map<String, TableRelation> getTableRelationRightMap() {
		return tableRelationRightMap;
	}

	public void setTableRelationRightMap(
			Map<String, TableRelation> tableRelationRightMap) {
		this.tableRelationRightMap = tableRelationRightMap;
	}

	public Set<String> getTableNameLeftMissedSet() {
		return tableNameLeftMissedSet;
	}

	public void setTableNameLeftMissedSet(Set<String> tableNameLeftMissedSet) {
		this.tableNameLeftMissedSet = tableNameLeftMissedSet;
	}

	public Set<String> getTableNameRightMissedSet() {
		return tableNameRightMissedSet;
	}

	public void setTableNameRightMissedSet(Set<String> tableNameRightMissedSet) {
		this.tableNameRightMissedSet = tableNameRightMissedSet;
	}

}