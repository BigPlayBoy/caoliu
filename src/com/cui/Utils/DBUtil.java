package com.cui.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBC 的工具类
 * 
 * 其中包含: 获取数据库连接, 关闭数据库资源等方法.
 */
public class DBUtil {
	static Logger log = LoggerFactory.getLogger(DBUtil.class);

	// 处理数据库事务的
	// 提交事务
	public static void commit(Connection connection) {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 回滚事务
	public static void rollback(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 开始事务
	public static void beginTx(Connection connection) {
		if (connection != null) {
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static DataSource dataSource = null;

	// 数据库连接池应只被初始化一次.
	static {
		dataSource = new ComboPooledDataSource("TJNU");
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("获取连接失败");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			con = getConnection();
		}
		return con;
	}

	/**
	 * 有查询结果的关闭
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void releaseDB(ResultSet resultSet, Statement statement, Connection connection) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				// 数据库连接池的 Connection 对象进行 close 时
				// 并不是真的进行关闭, 而是把该数据库连接会归还到数据库连接池中.
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 没有查询结果
	 * 
	 * @param statement
	 * @param connection
	 */
	public static void releaseDB(Statement statement, Connection connection) {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				// 数据库连接池的 Connection 对象进行 close 时
				// 并不是真的进行关闭, 而是把该数据库连接会归还到数据库连接池中.
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
