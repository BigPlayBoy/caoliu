package com.cui.Utils;

import java.io.File;
import java.io.FileOutputStream;
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
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 使用get方法连接服务器
		String pathname = null;
		String photoname = null;
		String url = "http://www.chuantupian.com/4/uploads/2016/05/IMG_20160530_230313.jpg";
		downloadPhoto(pathname, photoname, url);
	}

	/**
	 * 
	 * @param pathname文件保存路径
	 * @param photoname文件名
	 * @param url文件的网络地址
	 * @return
	 */
	public static boolean downloadPhoto(String pathname, String photoname, String url) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		FileOutputStream fos;
		try {
			// 客户端开始向指定的网址发送请求
			HttpResponse response = httpclient.execute(httpGet);
			InputStream inputStream = response.getEntity().getContent();
			File file = new File(pathname);
			if (!file.exists()) {
				file.mkdirs();
			}
			fos = new FileOutputStream(photoname);
			byte[] data = new byte[3 * 1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}
			return true;
		} catch (IOException e) {
			log.error("在downloadPhoto(pathname,photoname,url)中："+e);
		}
		return false;
	}

	static CloseableHttpClient HTTPCLIENT = HttpClients.createDefault();

	public static CloseableHttpClient getHttpClient() {
		if (HTTPCLIENT != null) {
			return HTTPCLIENT;
		} else {
			return HttpClients.createDefault();
		}

	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public static InputStream doGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();
		
		HttpResponse response = null;
		try {
			// 客户端开始向指定的网址发送请求
			response = httpclient.execute(httpGet);
			InputStream inputStream = response.getEntity().getContent();
			return inputStream;
		} catch (IOException e) {
			log.error("打开网页出错" + e);
		}
		return null;
	}


	/**
	 *
	 * @param url
	 */
	public void post(String url) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		try {
			log.info("executing request " + httppost.getURI());
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
			log.error(e);
		} catch (UnsupportedEncodingException e1) {
			log.error(e1);
		} catch (IOException e) {
			log.error(e);
		} finally {
			// 关闭连接,释放资源
			//只有一个httpclient实例 不应该释放
//			try {
//				httpclient.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
		}
	}
	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	/**
	 * 
	 * @param url
	 * @param formparams
	 */
	public void post(String url,List<NameValuePair> formparams) {
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
			log.info("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (UnsupportedEncodingException e1) {
			log.error(e1);
		} catch (IOException e) {
			log.error(e);
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