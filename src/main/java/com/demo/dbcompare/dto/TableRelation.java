package com.demo.dbcompare.dto;

public class TableRelation {

	private String tableNameLeft;

	private String tableNameRight;

	public TableRelation() {
		super();
	}

	public TableRelation(String tableNameLeft, String tableNameRight) {
		super();
		this.tableNameLeft = tableNameLeft;
		this.tableNameRight = tableNameRight;
	}

	public String getTableNameLeft() {
		return tableNameLeft;
	}

	public void setTableNameLeft(String tableNameLeft) {
		this.tableNameLeft = tableNameLeft;
	}

	public String getTableNameRight() {
		return tableNameRight;
	}

	public void setTableNameRight(String tableNameRight) {
		this.tableNameRight = tableNameRight;
	}

}
