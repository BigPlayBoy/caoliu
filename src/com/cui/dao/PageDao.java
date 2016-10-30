package com.cui.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Stack;

import com.cui.Utils.DBUtil;
import com.cui.Utils.MD5;
import com.cui.bean.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//完成增删改查
public class PageDao {
    static Logger log = LoggerFactory.getLogger(PageDao.class);

    // 插入新的对象
    public boolean addPage(PageBean pageBean) {
        String sql = "insert into page(title,url,urlmd5,create_date) values(?,?,?,?)";
        Connection con = null;
        PreparedStatement pres = null;
        try {
            pres = con.prepareStatement(sql);
            pres.setString(1, pageBean.getTitle());
            pres.setString(2, pageBean.getPage_URL());
            pres.setString(3, pageBean.getUrl_md5());
            pres.setString(4, pageBean.getCreate_date());
            pres.execute();
        } catch (SQLException e) {
            log.info("插入数据出错" + e);
        }
        return false;
    }

    public static boolean isExist(String urlMd5) {
        String sql = "select * from page where urlmd5='" + urlMd5 + "';";
        Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            state = conn.createStatement();
            rs = state.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.releaseDB(rs, state, conn);
        }
        return false;

    }

    /**
     * 返回值为1本链接已存在 返回值为2链接不存在添加成功 返回值为3出错
     *
     * @param url
     * @return
     */
    public static int addUrl(String url, String title) {
        String urlmd5 = MD5.GetMD5Code(url);
        String sql = "insert into page_xinshidai(title,url,url_md5) values('" + title + "','" + url + "','" + urlmd5 + "');";
//        System.out.println(sql);
        Connection con = null;
        Statement state = null;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(sql);
            return 2;
        } catch (Exception e) {
            log.error("ops 出了点错" + sql);
            e.printStackTrace();
        } finally {
            DBUtil.releaseDB(state, con);
        }
        return 0;
    }

    public static int addUrl(Stack<String> urls) {
        // 一次添加一个栈的数据
        String sqlhead = "insert into page(page_url,url_md5,create_date) values";
        String sqlbody = "";
        String sqlend = ";";
        String create_date = String.valueOf(new Date().getTime());
        boolean flag = true;
        while (!urls.isEmpty()) {
            String url = urls.pop();
            String urlMd5 = MD5.GetMD5Code(url);
            if (PageDao.isExist(urlMd5)) {
                continue;
            }
            if (!flag) {
                sqlbody = sqlbody + " ,('" + url + "','" + urlMd5 + "','" + create_date + "') ";
            } else {
                flag = false;
                sqlbody = sqlbody + " ('" + url + "','" + urlMd5 + "','" + create_date + "') ";
            }
        }
        String sql = sqlhead + sqlbody + sqlend;
        log.info("SQL语句为" + sql);
        if (update(sql)) return 2;
        return 0;
    }

    public static int savePageBean(PageBean pageBean) {
        String urlMd5 = pageBean.getUrl_md5();
        if (PageDao.isExist(urlMd5)) {
            return 1;
        }
        String sql = "insert into page1(title,page_url,url_md5,create_date) values('" + pageBean.getTitle() + "','" + pageBean.getPage_URL() + "','" + urlMd5 + "','" + pageBean.getCreate_date() + "');";
        if (update(sql)) return 2;
        return 3;
    }

    private static boolean update(String sql) {
        Connection con = null;
        Statement state = null;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            log.info("update数据库 出了点错" + sql + e);
        } finally {
            DBUtil.releaseDB(state, con);
        }
        return false;
    }
}
