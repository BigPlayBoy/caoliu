package com.cui.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetTool {

	static Logger log = LoggerFactory.getLogger(NetTool.class);

	public static void main(String[] args) {
		String URL = "http://dc.ididcl.co/index.php";
		System.out.println("获取的cookie为" + getOffCookie(URL));
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String doGet(String url, String cookie) {
		String result = "";
		BufferedReader in = null;
		try {
			// String urlNameString = url + "?" + param;
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			//System.out.println(realUrl);
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Host", "dz.a5v.biz");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
			connection.setRequestProperty("DNT", "1");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");

			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.setRequestProperty("Cookie", getCookie(url));
			// 建立实际的连接
			connection.connect();
			if ("gzip".equals(connection.getContentEncoding())) {
				// 使用了gzip加密
				in = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(connection.getInputStream()), "gbk"));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
			}
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.info("发送GET请求出现异常！" + e);
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
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String doGetPhoto(String url) {
		String result = "";
		BufferedReader in = null;
		
		try {
			// String urlNameString = url + "?" + param;
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Host", "www.datuimg.com");
			connection.setRequestProperty("Connection", "keep-alive");
			 connection.setRequestProperty("Cache-Control", "max-age=0");
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
			connection.setRequestProperty("DNT", "1");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			// connection.setRequestProperty("Referer",
			// "http://jw.tjnu.edu.cn/jwgl/cjgl/bbdy/print.php");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			 connection.setRequestProperty("Cookie","__cfduid=d8b0d8e773cb96e121c5f605cf4eea1561464532258");
			// 建立实际的连接
			connection.connect();
			if ("gzip".equals(connection.getContentEncoding())) {
				// 使用了gzip加密
//				in = new BufferedReader(
//						new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
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
			log.info("发送GET请求出现异常！" + e);
			// System.out.println("发送GET请求出现异常！" + e);
			// e.printStackTrace();
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
	public static String getOffCookie(String URL){
		String cookie=null;
		File filein=new File("resources/cookie.txt");
		BufferedReader in = null;
		if(filein.exists()){
			//文件存在  直接读取
			System.out.print("文件存在 直接调用");
				try {
					 in=new BufferedReader(new InputStreamReader(new FileInputStream(filein), "utf-8"));
					String s=null;
					while((s=in.readLine())!=null){
						cookie+=s;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}else{
			//文件不存在 调用getCookie();
			System.out.print("文件不存在 网络获取");
			cookie=getCookie(URL);
			File fp = new File("resources/cookie.txt");
			PrintWriter pfp = null;
			try {
				pfp = new PrintWriter(fp);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			pfp.print(cookie);
			pfp.close();
		}
		return cookie;
	}
	
	public static String getCookie(String url) {
			String cookie=null;
			try {
				// 尝试从网络获取
				String urlNameString = url;
				URL realUrl = new URL(urlNameString);
				// 打开和URL之间的连接
				URLConnection connection = realUrl.openConnection();
				// 设置通用的请求属性
				connection.setRequestProperty("Host", "dz.a5v.biz");
				connection.setRequestProperty("Connection", "keep-alive");
				connection.setRequestProperty("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
				connection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
				connection.setRequestProperty("DNT", "1");
				connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
				connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				// 建立实际的连接
				connection.connect();
				cookie = connection.getHeaderField("Set-Cookie");
			} catch (Exception e) {
				log.info("get cookie出现异常！" + e);
			}
		return cookie;
	}
	/**
	 * 保存成网页
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	public static boolean savecookie(String result)  {
		File fp = new File("resources/cookie.txt");
		//String str = result;
		PrintWriter pfp = null;
		try {
			pfp = new PrintWriter(fp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pfp.print(result);
		pfp.close();
		return true;
	}
	
}
