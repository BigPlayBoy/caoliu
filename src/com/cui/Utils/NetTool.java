package com.cui.Utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetTool {

    static Logger logger = LoggerFactory.getLogger(NetTool.class);

    public static void main(String[] args) {
        String URL = "http://dc.ididcl.co/index.php";
        System.out.println("获取的cookie为" + getOffCookie(URL));
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param cookie 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String doGet(String url, String cookie) {
        String result = "";
        BufferedReader in = null;
        try {
            // String urlNameString = url + "?" + param;
            String urlNameString = url;
            URLConnection urlConnection = null;
            urlConnection = getUrlConnection(cookie, urlNameString);
            // 建立实际的连接
            urlConnection.connect();
            if ("gzip".equals(urlConnection.getContentEncoding())) {
                // 使用了gzip加密
                in = new BufferedReader(
                        new InputStreamReader(new GZIPInputStream(urlConnection.getInputStream()), "gbk"));
            } else {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "gbk"));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("发送GET请求出现异常！" + e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    //从本地获取cookie
    static String getOffCookie(String URL) {
        String cookie;
        File file = new File("resources/cookie.txt");
        if (file.exists()) {
            logger.info("文件存在，直接调用");
            cookie = FileTool.readFile(file, "utf-8");
        } else {
            System.out.print("文件不存在 网络获取");
            cookie = getCookie(URL);
            saveCookie(cookie);
        }
        return cookie;
    }

    private static String getCookie(String url) {
        String cookie = null;
        try {
            // 打开和URL之间的连接
            URLConnection connection = getUrlConnection(cookie, url);
            connection.connect();
            cookie = connection.getHeaderField("Set-Cookie");
        } catch (Exception e) {
            logger.info("get cookie出现异常！" + e);
        }
        return cookie;
    }

    /**
     * 保存cookie
     *
     * @param result
     * @return
     */
    private static boolean saveCookie(String result) {
        File file = new File("resources/cookie.txt");
        return FileTool.saveFile(result, file);
    }

    /**
     * urlconnection通用设置
     *
     * @param cookie cookie
     * @param urlNameString url
     * @return
     * @throws IOException
     */
    private static URLConnection getUrlConnection(String cookie, String urlNameString) throws IOException {
        URLConnection urlConnection;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        //System.out.println(realUrl);
        urlConnection = realUrl.openConnection();
        // 设置通用的请求属性
        urlConnection.setRequestProperty("Host", "dz.a5v.biz");
        urlConnection.setRequestProperty("Connection", "keep-alive");
        urlConnection.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        urlConnection.setRequestProperty("DNT", "1");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        urlConnection.setRequestProperty("Cookie", cookie);
        return urlConnection;
    }

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
        InputStream inputStream = null;
        try {
            // 客户端开始向指定的网址发送请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("获取失败 跳过此文件");
                return false;
            }
            inputStream = httpResponse.getEntity().getContent();
            savePhoto(pathname, photoName, inputStream);
        } catch (IOException e) {
            logger.error("在downloadPhoto中IO异常！" + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    static boolean savePhoto(String pathname, String photoName, InputStream inputStream) throws IOException {
        FileOutputStream fileOutputStream = null;
        fileOutputStream = new FileOutputStream(pathname + "/" + photoName);
        byte[] data = new byte[20 * 1024];
        int len;
        while ((len = inputStream.read(data)) != -1) {
            fileOutputStream.write(data, 0, len);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        return true;
    }
}
