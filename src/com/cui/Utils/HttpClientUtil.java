package com.cui.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class HttpClientUtil {
    private static Logger logger = Logger.getLogger(HttpClientUtil.class);

    public static void main(String[] args) {

    }


    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static CloseableHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        } else {
            return HttpClients.createDefault();
        }
    }

    /**
     * @param url url
     * @return inputStream
     */
    public static InputStream doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();

        HttpResponse response = null;
        try {
            // 客户端开始向指定的网址发送请求
            response = httpclient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            logger.error("打开网页出错" + e + "\n错误网址为" + url);
        }
        return null;
    }


    /**
     * @param url
     */
    public void post(String url) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        try {
            logger.info("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     *
     * @param url        url
     * @param formparams canshu
     */
    public void post(String url, List<NameValuePair> formparams) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列
        //List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        //formparams.add(new BasicNameValuePair("type", "house"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            logger.info("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    logger.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            // 关闭连接,释放资源
//			try {
//				httpclient.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
        }
    }
}