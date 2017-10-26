package com.demo.dbcompare.dto;

import java.util.ArrayList;
import java.util.List;

public class DataGridDto<T> {

	private Integer total = 0;

	private List<T> rows = new ArrayList<T>();

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
