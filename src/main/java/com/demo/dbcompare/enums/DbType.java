package com.demo.dbcompare.enums;

/**
 * 支持的数据库类型枚举类
 * @author: jie.deng
 * @time: 2017年3月6日 下午11:12:51
 */
public enum DbType {
	
	MySQL("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql://<ip>:<port>/<database_name>", 3306), 
	Oracle("Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@<ip>:<port>:<database_name>", 1521), 
	None("", "", "", null);
	
	// Oracle|MySQL等常用数据库
	private String dbTypeName;

	// 数据库JDBC驱动类
	private String driver;

	// 数据库JDBC url模板
	private String url;

	// 数据库默认端口
	private Integer port;

	private DbType(String dbTypeName, String driver, String url, Integer port) {
		this.dbTypeName = dbTypeName;
		this.driver = driver;
		this.url = url;
		this.port = port;
	}

	public String getDbTypeName() {
		return dbTypeName;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public Integer getPort() {
		return port;
	}

	public static DbType of(String dbTypeName) {
		for (DbType dbType : DbType.values()) {
			if (dbType.getDbTypeName().equalsIgnoreCase(dbTypeName)) {
				return dbType;
			}
		}
		return DbType.None;
	}

}