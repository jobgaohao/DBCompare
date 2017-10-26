package com.demo.dbcompare.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dbcompare.dto.CompareTast;
import com.demo.dbcompare.dto.DataGridDto;
import com.demo.dbcompare.dto.DbCompareDiff;
import com.demo.dbcompare.service.TableColumnInfoService;


@RestController
@RequestMapping(value="/")
public class CompareController {
	
	@Autowired
	private TableColumnInfoService tableColumnInfoService;
	
	@RequestMapping(value="compare")
	public DataGridDto<DbCompareDiff> compare(CompareTast compareTast,
			@RequestParam(value="page", defaultValue="1")int pageNumber,
			@RequestParam(value="rows", defaultValue="100")int pageSize){
		DataGridDto<DbCompareDiff> dataGridDto = new DataGridDto<DbCompareDiff>();
		if (StringUtils.isEmpty(compareTast.getConnNameLeft())
				|| StringUtils.isEmpty(compareTast.getConnNameRight())) {
			return dataGridDto;
		}
		tableColumnInfoService.seizeTableColumnInfo(compareTast);
		
		List<DbCompareDiff> diffList = DbCompareDiff.genDiff(compareTast);
		
		dataGridDto.setTotal(diffList.size());
		int fromIndex = ((pageNumber>=1 ? pageNumber : 1) - 1) * pageSize ;
		int toIndex = Math.min(fromIndex+pageSize, diffList.size());
		dataGridDto.setRows(diffList.subList(fromIndex, toIndex));
		return dataGridDto;
	}



}
