package com.cui.dao;

import com.cui.Utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by jarvis on 2016/10/31.
 * 数据库的增删改查
 */
public class DBTool {

    private static Logger logger = LoggerFactory.getLogger("DBTool.class");


    /**
     * 查询数据库,这个有问题！！！！
     *
     * @param sql 待执行语句
     * @return 查询结果集
     */
    private static ResultSet select(String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            logger.error("查询数据库出错" + e);
            logger.error("查询数据库的语句为：" + sql);
            return null;
        } finally {
            DBUtil.releaseDB(statement, connection);
        }
    }

    /**
     * 删除数据库某条信息
     *
     * @param sql 待执行语句
     * @return 真/假
     */
    private static Boolean delete(String sql) {
        return update(sql);
    }

    /**
     * 插入数据库
     *
     * @param sql 待执行语句
     * @return 真/假
     */
    private static Boolean insert(String sql) {
        return update(sql);
    }

    /**
     * 更新数据库，插入，删除，也是调用这个
     *
     * @param sql 待执行语句
     * @return 返回是否成功
     */
    private static Boolean update(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.error("更新数据库出错" + e);
            logger.error("更新数据库的语句为：" + sql);
            return false;
        } finally {
            DBUtil.releaseDB(statement, connection);
        }
    }

    public static void main(String[] args) {
        //测试增删改查
        String insert = "insert into page(title,page_url) values('ceshi','baidu.com')";
        String delete = "delete from page where id=20581";
        String update = "update page set title='ceshiyongli' where id=20581";
        String select = "select * from page where id= 20581";
//        insert(insert);
//        update(update);
//        ResultSet select1 = select(select);
//        try {
//            while (select1.next()){
//                System.out.println(select1.getString(2));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        delete(delete);
    }
}
