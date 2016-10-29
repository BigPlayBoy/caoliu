package com.cui.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

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
	 * @param url
	 *            发送请求的URL
	 * @param cookie
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String doGet(String url, String cookie) {
		String result = "";
		BufferedReader in = null;
		try {
			// String urlNameString = url + "?" + param;
			String urlNameString = url;
			URLConnection urlConnection=null;
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
		// 使用finally块来关闭输入流
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


	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	static String doGetPhoto(String url,String cookie) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			// 打开和URL之间的连接
			URLConnection connection = getUrlConnection(cookie, urlNameString);
			// 建立实际的连接
			connection.connect();
			if ("gzip".equals(connection.getContentEncoding())) {
				in = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(connection.getInputStream()), "gb2312"));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gb2312"));
			}
			String line=new String("gb2312");
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.info("发送GET请求出现异常！" + e);
		}
		// 使用finally块来关闭输入流
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
	 static String getOffCookie(String URL){
		String cookie;
		File filein=new File("resources/cookie.txt");
		if(filein.exists()){
			//文件存在  直接读取
			logger.info("文件存在，直接调用");
			cookie=FileTool.readFile(filein,"utf-8");
		}else{
			//文件不存在 调用getCookie();
			System.out.print("文件不存在 网络获取");
			cookie=getCookie(URL);
			saveCookie(cookie);
		}
		return cookie;
	}
	
	 private static String getCookie(String url) {
			String cookie=null;
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
	 * @param result
	 * @return
	 */
	 private static boolean saveCookie(String result)  {
		File file = new File("resources/cookie.txt");
		return FileTool.saveFile(result,file);
	}

	/**
	 * urlconnection通用设置
	 * @param cookie
	 * @param urlNameString
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
}
