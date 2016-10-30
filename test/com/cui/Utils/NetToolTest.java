package com.cui.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by jarvis on 2016/10/29.
 * 测试NetTool
 */
public class NetToolTest {
    private static Logger logger = LoggerFactory.getLogger("NetToolTest.class");

    public static void main(String[] args) {
        String url = "http://ultraimg.com/images/2016/10/22/xVbm.jpg";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        InputStream inputStream = null;
        String pathname = "e:/";
        String photoName = "3.jpg";
        try {
            // 客户端开始向指定的网址发送请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("获取失败 跳过此文件");
            }
            inputStream = httpResponse.getEntity().getContent();
            NetTool.savePhoto(pathname, photoName, inputStream);
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
    }
}
