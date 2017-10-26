package com.demo.dbcompare.model;

import org.apache.commons.lang3.StringUtils;

/**
 * mysql:`information_schema`.`COLUMNS` 
 * oracle:SYS.USER_TAB_COLUMNS
 * 
 * @Description: TODO
 * @author: jie.deng
 * @time: 2017年3月9日 下午9:11:10
 */
public class TableColumnInfo {

	// mysql:TABLE_NAME oracle:TABLE_NAME
	private String tableName;

	// mysql:COLUMN_NAME oracle:COLUMN_NAME
	private String columnName;

	// mysql:DATA_TYPE oracle:DATA_TYPE
	private String dataType;

	// mysql:字符串CHARACTER_MAXIMUM_LENGTH oracle:DATA_LENGTH
	private String dataLength;

	// mysql:NUMERIC_PRECISION oracle:DATA_PRECISION
	private String dataPrecision;

	// mysql:NUMERIC_SCALE oracle:DATA_SCALE
	private String dataScale;

	// mysql:IS_NULLABLE oracle:NULLABLE
	private String nullable;

	// mysql:COLUMN_DEFAULT oracle:DATA_DEFAULT
	private String dataDefault;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(String dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public String getDataScale() {
		return dataScale;
	}

	public void setDataScale(String dataScale) {
		this.dataScale = dataScale;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getDataDefault() {
		return dataDefault;
	}

	public void setDataDefault(String dataDefault) {
		this.dataDefault = dataDefault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result
				+ ((dataDefault == null) ? 0 : dataDefault.hashCode());
		result = prime * result
				+ ((dataLength == null) ? 0 : dataLength.hashCode());
		result = prime * result
				+ ((dataPrecision == null) ? 0 : dataPrecision.hashCode());
		result = prime * result
				+ ((dataScale == null) ? 0 : dataScale.hashCode());
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result
				+ ((nullable == null) ? 0 : nullable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableColumnInfo other = (TableColumnInfo) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (dataDefault == null) {
			if (other.dataDefault != null)
				return false;
		} else if (!StringUtils.trim(dataDefault).equals(StringUtils.trim(other.dataDefault)))
			return false;
		if (dataLength == null) {
			if (other.dataLength != null)
				return false;
		} else if (!dataLength.equals(other.dataLength))
			return false;
		if (dataPrecision == null) {
			if (other.dataPrecision != null)
				return false;
		} else if (!dataPrecision.equals(other.dataPrecision))
			return false;
		if (dataScale == null) {
			if (other.dataScale != null)
				return false;
		} else if (!dataScale.equals(other.dataScale))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (nullable == null) {
			if (other.nullable != null)
				return false;
		} else if (!nullable.equals(other.nullable))
			return false;
		return true;
	}

}