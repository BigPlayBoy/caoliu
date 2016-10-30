package com.cui.Utils;

import com.cui.bean.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Stack;

public class Util {
    static Logger logger = LoggerFactory.getLogger(Util.class);
    // 下载图片


    public static void addurl(int id) {
        String url = "http://dz.a5v.biz/";
        Connection con = null;
        Statement state = null;
        ResultSet resultSet = null;
        PageBean pageBean = new PageBean();

        String sql = "select title,url_md5,page_url from page where id=" + id;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            resultSet = state.executeQuery(sql);
            pageBean.setId(id - 203);
            pageBean.setTitle(resultSet.getString(1));
            pageBean.setPage_URL(resultSet.getString(3));
            pageBean.setUrl_md5(resultSet.getString(2));
            pageBean.setCreate_date(new Date().toString());
        } catch (Exception e) {
            logger.error("ops 出了点错" + e);
            e.printStackTrace();
        } finally {
            DBUtil.releaseDB(state, con);
        }

        String insertSql = "insert into page values(" + pageBean.getId() + ",'" + pageBean.getTitle() + "','" + pageBean.getUrl_md5() + "','" + pageBean.getPage_URL() + "','" + pageBean.getCreate_date() + "')";
        System.out.println(insertSql);

        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(insertSql);
            logger.info("更新成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String deleteSql = "delete page where id=" + id;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(deleteSql);
            logger.info("删除成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        int begin = 204;
//        int end = 10477;
//        while (begin < end) {
//            addurl(begin++);
//        }
//        System.out.println("end" + begin + "\t" + end);
        deleteFiles();
    }


    private static boolean deleteFiles() {
        String reg = ".*tilqs.*";
        int i = 0;
        //第一步  获得当前项目的路径
        String path = System.getProperty("user.dir");
        System.out.println(path);
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File file1 : tempList) {
            if (file1.isFile() || file1.toString().matches(reg)) {
                {
                    file1.delete();
                    i++;
                }
            }
        }
        logger.info("一共删除了" + i + "个文件");
        return true;
    }

    public static Stack<String> getPhotoUrl(int begin, int end) {
        Connection con = null;
        Statement state = null;
        ResultSet rs = null;

        Stack<String> urlStack = new Stack<String>();
        String sql = "select url from page where url like '%html' limit " + begin + "," + end;
        System.err.println(sql);
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            rs = state.executeQuery(sql);
            while (rs.next()) {
                urlStack.push(rs.getString("url"));
                // urlStack.push(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("从数据库获取信息出错" + e);
        } finally {
            DBUtil.releaseDB(rs, state, con);
        }
        return urlStack;
    }
}
