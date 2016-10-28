package com.cui.Utils;

import com.cui.bean.PageBean;
import com.cui.main.Main;
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

public class Util {
    static Logger log = LoggerFactory.getLogger(Util.class);
    // 下载图片

    /**
     * @param imgUrl  图片的网络路径
     * @param fileURL 本地保存地址
     */
    public static void downloadImage(String imgUrl, String fileURL) {
        try {
            // 创建流
            BufferedInputStream in = new BufferedInputStream(new URL(imgUrl).openStream());
            // 生成图片名
            int index = imgUrl.lastIndexOf("/");
            String sName = imgUrl.substring(index + 1, imgUrl.length());
            System.out.println(sName);
            // 存放地址
            File img = new File(fileURL + sName);
            // 生成图片
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));
            byte[] buf = new byte[2048];
            int length = in.read(buf);
            while (length != -1) {
                out.write(buf, 0, length);
                length = in.read(buf);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            System.out.println("ops 出了点错");
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
            System.out.println("更新成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String deleteSql = "delete page where id=" + id;
        try {
            con = DBUtil.getConnection();
            state = con.createStatement();
            state.executeUpdate(deleteSql);
            System.out.println("删除成功！");
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
            if (file1.isFile()) {
                {
                    if (file1.toString().matches(reg)) {
//                        System.out.println("Delete:" + file1);
                        file1.delete();
                        i++;
                    }
                }
            }
        }
        log.info("一共删除了" + i + "个文件");
        return true;
    }
}
