package com.cui.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class downLoadImage implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("downLoadImage.class");

    /**
     * @param pathname  文件保存路径
     * @param photoName 文件名
     * @param url       文件的网络地址
     * @return
     */
    public static boolean downloadPhoto(String pathname, String photoName, String url) {
        logger.info("开始下载" + photoName);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            // 客户端开始向指定的网址发送请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("获取失败 跳过此文件");
                return false;
            }
            inputStream = httpResponse.getEntity().getContent();
            File file = new File(pathname);
            if (!file.exists()) {
                file.mkdirs();
            }
            fileOutputStream = new FileOutputStream(pathname + "/" + photoName);
            byte[] data = new byte[20 * 1024];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
                fileOutputStream.flush();
            }
            return true;
        } catch (IOException e) {
            logger.error("在downloadPhoto中IO异常！" + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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
            System.out.println("从数据库获取信息出错");
        } finally {
            DBUtil.releaseDB(rs, state, con);
        }
        return urlStack;
    }

    public static URI createFolder(String folderName) {
        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdirs();// 如果路径不存在，创建路径
        }
        // 将创建的路径返回
        return file.toURI();
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        String pathname = "e:/down";
        String url = "http://imgbbs.heiguang.net/forum/201510/06/104432cjc7c8tx7xxqqkgq.jpg";
        String photoName = url.substring(url.lastIndexOf("/") + 1, url.length());
        System.out.println(photoName);
        downloadPhoto(pathname, photoName, url);
    }
}
