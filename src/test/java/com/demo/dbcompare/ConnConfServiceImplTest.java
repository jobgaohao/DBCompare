package com.demo.dbcompare;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo.dbcompare.dto.CompareTast;
import com.demo.dbcompare.dto.DbCompareDiff;
import com.demo.dbcompare.enums.DbType;
import com.demo.dbcompare.service.TableColumnInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:applicationContext.xml")
@Ignore
public class ConnConfServiceImplTest {
	
	@Autowired
	private TableColumnInfoService tableColumnInfoService;
	
	@Test
	public void testDbDiff() {
		CompareTast compareTast = new CompareTast();
		compareTast.setConnNameLeft("testB");
		compareTast.setUrlLeft("jdbc:mysql://127.0.0.1:3306/test");
		compareTast.setUsernameLeft("root");
		compareTast.setPasswordLeft("root");
		compareTast.setConnNameRight("jeesit");
		compareTast.setUrlRight("jdbc:mysql://127.0.0.1:3306/jeesite");
		compareTast.setUsernameRight("root");
		compareTast.setPasswordRight("root");
		tableColumnInfoService.seizeTableColumnInfo(compareTast);
		DbCompareDiff.genDiff(compareTast);	
	}
	
	@Test
	public void testTableDiff() {
		CompareTast compareTast = new CompareTast();
		compareTast.setConnNameLeft("testB");
		compareTast.setUrlLeft("jdbc:mysql://127.0.0.1:3306/test");
		compareTast.setUsernameLeft("root");
		compareTast.setPasswordLeft("root");
		compareTast.setTableNameListLeft(Arrays.asList("sys_menu"));
		compareTast.setConnNameRight("jeesit");
		compareTast.setUrlRight("jdbc:mysql://127.0.0.1:3306/jeesite");
		compareTast.setUsernameRight("root");
		compareTast.setPasswordRight("root");
		compareTast.setTableNameListRight(Arrays.asList("sys_menu"));
		tableColumnInfoService.seizeTableColumnInfo(compareTast);
		DbCompareDiff.genDiff(compareTast);	
	}
	
	@Test
	public void testReplace(){
		String str = DbType.MySQL.getUrl()
				.replace("<ip>", "127.0.0.1")
				.replace("<port>", Integer.valueOf(3306).toString())
				.replace("<database_name>", "db_compare");		
		System.out.println(str);
	}

}
