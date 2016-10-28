package com.cui.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static boolean isExist(String urlmd5) {
        String sql = "select * from page where urlmd5='" + urlmd5 + "';";
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
     * 返回值为1本链接已存在 不需要添加 返回值为2链接不存在添加成功 返回值为3出错
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
        String sqlhead = "insert into page(page_url,url_md5) values";
        String sqlbody = "";
        String sqlend = ";";
        boolean flag = true;
        while (!urls.isEmpty()) {
            String url = urls.pop();
            String urlmd5 = MD5.GetMD5Code(url);
            if (PageDao.isExist(urlmd5)) {
                continue;
            }
            if (flag) {
                flag = false;
                sqlbody = sqlbody + " ('" + url + "','" + urlmd5 + "') ";
            } else {
                sqlbody = sqlbody + " ,('" + url + "','" + urlmd5 + "') ";
            }
        }
        String sql = sqlhead + sqlbody + sqlend;
        System.out.println(sql);
        Connection con = null;
        Statement state = null;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(sql);
            return 2;
        } catch (Exception e) {
            log.info("ops 出了点错" + e);
        } finally {
            DBUtil.releaseDB(state, con);
        }
        return 0;
    }

    public static int save(PageBean pageBean) {
        String urlmd5 = MD5.GetMD5Code(pageBean.getPage_URL());
        if (PageDao.isExist(urlmd5)) {
            return 1;
        }
        String sql = "insert into page1(title,page_url,url_md5) values('" + pageBean.getTitle() + "','" + pageBean.getPage_URL() + "','" + urlmd5 + "');";
        Connection con = null;
        Statement state = null;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(sql);
            return 2;
        } catch (Exception e) {
            log.info("ops 出了点错" + sql + e);
        } finally {
            DBUtil.releaseDB(state, con);
        }
        return 3;
    }
}
