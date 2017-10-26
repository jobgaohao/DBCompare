package com.demo.dbcompare.factory;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.demo.dbcompare.dto.ConnConf;

/**
 * 根据数据库连接信息获取Mapper接口的实现类对象
 * @author: jie.deng
 * @time: 2017年4月12日 上午9:52:44
 */
public class MapperFactoty {
	
	private static final JdbcTransactionFactory DEFAULT_TRANSACTION_FACTORY = new JdbcTransactionFactory();
	private static final String DEFAULT_DEVELOPMENT_ID = "development";

	/**
	 * 根据数据库连接信息获取Mapper接口的实现类对象
	 * @param connConf
	 * @param clz
	 * @return
	 * @author: jie.deng
	 * @time: 2017年4月12日 上午9:58:23
	 */
	public static <T> T getMapper(ConnConf connConf, Class<T> clz){
		DataSource dataSource = new UnpooledDataSource(
				connConf.getDriver(),
				connConf.getUrl(),
				connConf.getUsername(),
				connConf.getPassword());
		Environment environment = new Environment(DEFAULT_DEVELOPMENT_ID, 
				DEFAULT_TRANSACTION_FACTORY, 
				dataSource);
		Configuration configuration = new Configuration(environment);
		String databaseId = new VendorDatabaseIdProvider().getDatabaseId(dataSource);
		//setDatabaseId要放在addMapper前面
		configuration.setDatabaseId(databaseId);	
		configuration.addMapper(clz);
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession session = factory.openSession();
		return session.getMapper(clz);
	}
	
	
}
