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

	static void PrintMap(Map<Integer, String> map) {
		System.out.println("���Map�е�����");
		Set<Integer> set = map.keySet(); // keyװ��set��
		Iterator<Integer> it = set.iterator(); // ����set�ĵ����� װ��keyֵ
		while (it.hasNext()) {
			Integer key = it.next();
			String value = (String) map.get(key);
			System.out.println(key + " " + value);
		}
		System.out.println("������");
	}

	// 获得网址所在的匹配区域部分
	// 个人信息在第三个table里
	// 成绩在第四个table里
	// 从指定的字符串中 匹配需要的范围 只需要输入匹配的开始 和结尾
	// 这个可以制定开头的大概位置
	static String getContentArea(String urlContent, String strAreaBegin, String strAreaEnd, int fromIndex) {
		int pos1 = 0, pos2 = 0;
		int i;
		for (i = 1; i < fromIndex; i++) {
			pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
			// System.out.println("在循环里面 pos1的值为："+pos1);
		}
		pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
		pos2 = urlContent.indexOf(strAreaEnd, pos1);
		// System.out.println("pos1:" + pos1 + "pos2" + pos2);
		String ContentArea = urlContent.substring(pos1, pos2).replaceAll("\t|\n", "").replaceAll(" ", "");
		// System.out.println(ContentArea);
		return ContentArea;
	}

	// 这个会从第一个匹配的开始
	static String getContentArea(String urlContent, String strAreaBegin, String strAreaEnd) {
		int pos1 = 0, pos2 = 0;
		int i;
		for (i = 0; i < 2; i++) {
			pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
			System.out.println("在循环里面 pos1的值为：" + pos1);
		}

		pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
		pos2 = urlContent.indexOf(strAreaEnd, pos1);
		System.out.println("pos1:" + pos1 + "pos2" + pos2);
		String ContentArea = urlContent.substring(pos1, pos2).replaceAll("\t|\n", "").replaceAll(" ", "");
		// System.out.println(ContentArea);
		return ContentArea;
	}

	static String RegexStr(String targetStr, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(targetStr);
		// 需要保存数据
		int i = 1;
		while (matcher.find()) {
			// Grade grade = new Grade();
			System.out.print("匹配结果" + i++);
			System.out.println(matcher.group());// .replaceAll("<td>",
												// "").replaceAll("&nbsp;", "")
		}
		System.out.println("lalalala");
		if (matcher.find()) {
			return matcher.group();
		}
		return "nothing";
	}

	// 写一个清除网页属性的函数
	// 将<table....> <tr......> <td......>分别替换成<table><tr><td>
	static String replacettt(String string) {
		return string.replaceAll("[\\<]td.*?[\\>]", "<td>").replaceAll("[\\<]tr.*?[\\>]", "<tr>")
				.replaceAll("[\\<]table.*?[\\>]", "<table>");
		// return string;
	}

	/**
	 * 将<table....>替换成
	 * <table>
	 * 
	 * @param string
	 * @return
	 */
	static String replaceTable(String string) {
		return string.replaceAll("[\\<]table.*?[\\>]", "<table>");
	}

	/**
	 * 将<td......>替换成
	 * <td>
	 * 
	 * @param string
	 * @return
	 */
	static String replaceTd(String string) {
		return string.replaceAll("[\\<]td.*?[\\>]", "<td>");
	}

	/**
	 * 将 <tr......>分别替换成
	 * <tr>
	 * 
	 * @param string
	 * @return
	 */
	static String replaceTr(String string) {
		return string.replaceAll("[\\<]tr.*?[\\>]", "<tr>");
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	static String replaceSpan(String string) {
		return string.replaceAll("[\\<]span.*?[\\>]", "<span>");
	}

	/**
	 * 保存成网页
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	public static boolean savepage(String result, String xuehao) {
		File fp = new File(xuehao + ".html");
		String str = result;
		PrintWriter pfp = null;
		try {
			pfp = new PrintWriter(fp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pfp.print(str);
		pfp.close();
		return true;
	}

	public static String readPage(String pagename) throws IOException {
		String string = null;
		@SuppressWarnings("resource")
		// 网页的编码是gb2312 但文本的编码是utf-8......
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(pagename), "utf-8"));
		String line = null;
		while ((line = in.readLine()) != null) {
			string += line;
		}
		return string;

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
	public static String doGet(String url) {
		String result = "";
		BufferedReader in = null;
		
		try {
			// String urlNameString = url + "?" + param;
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Host", "dc.ididcl.co");
			connection.setRequestProperty("Connection", "keep-alive");
			// connection.setRequestProperty("Cache-Control", "max-age=0");
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
			 connection.setRequestProperty("Cookie",getOffCookie(url));
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
						new InputStreamReader(new GZIPInputStream(connection.getInputStream()), "gb2312"));
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gb2312"));
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
	

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, String cookie) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Host", "jw.tjnu.edu.cn");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Length", "45");//
			conn.setRequestProperty("Cache-Control", "max-age=0");//
			conn.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");//
			conn.setRequestProperty("Origin", "http://jw.tjnu.edu.cn");//
			conn.setRequestProperty("Upgrade-Insecure-Requests", "1");//
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36");//
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//
			conn.setRequestProperty("DNT", "1");//
			conn.setRequestProperty("Referer", "http://jw.tjnu.edu.cn/");//
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");//
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");//
			conn.setRequestProperty("Cookie", cookie);
			// System.out.println("POST提交的cookie为：\n" + cookie);
			// System.out.println(conn);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			// 这里成功拿到cookie
			// String head = conn.getHeaderField("Set-Cookie");
			// System.out.println("返回的Cookie is \n\t" + head);
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.info("发送 POST 请求出现异常！" + e);
			// System.out.println("发送 POST 请求出现异常！" + e);
			// e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
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
