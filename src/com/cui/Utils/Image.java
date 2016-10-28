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

public class Image implements Runnable {
	String title;
	URI photourl;
	String folderPath;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URI getPhotourl() {
		return photourl;
	}

	public void setPhotourl(URI photourl) {
		this.photourl = photourl;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Image(String title, URI photourl, String folderPath) {
		super();
		this.title = title;
		this.photourl = photourl;
		this.folderPath = folderPath;
	}

	public Image() {
		super();
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
		FileOutputStream fos = null;
//		BufferedWriter bufferw=new BufferedWriter(new OutputStreamWriter(fos));
		InputStream inputStream = null;
//		BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
		try {
			// 客户端开始向指定的网址发送请求
			HttpResponse response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("获取失败 跳过此文件");
				return false;
			}
			 inputStream = response.getEntity().getContent();
			File file = new File(pathname);
			if (!file.exists()) {
				file.mkdirs();
			}
			fos = new FileOutputStream(pathname + "/" + photoname);
			byte[] data = new byte[20 * 1024];
			int len = 0;

			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
				fos.flush();
			}
			
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("在downloadPhoto中IO异常！"+e);
		}finally{
			try {
				if(inputStream!=null){
					inputStream.close();
				}
				if(fos!=null){
					fos.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}




	public static void downloadImage(String pageUrl) {
		Document doc = null;
		Elements tables = null;
		Elements input = null;
		String pathname = "e://社区";
		String title = null;
		String sName="null";
		InputStream ins = HttpClientUtil.doGet(pageUrl);
		try {
			// 将获取的流对象化
			doc = Jsoup.parse(ins, "gb2312", "");
			title = doc.title();
			tables = doc.getElementsByTag("table");
			input = tables.get(2).children().get(0).children().get(0).children().get(1).getElementsByAttribute("src");

			for (Iterator iterator = input.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				System.out.println("正在下载" + element.attr("src"));
				//将下载的函数移到了下载类
				// 生成图片名
				int index = pageUrl.lastIndexOf("/");
				 sName = pageUrl.substring(index + 1, pageUrl.length());
				DownloadUtils.downloadPhoto(pathname + "/" + title, sName, element.attr("src"));
				System.out.println("\n下载完成" + element.attr("src"));
			}
		} catch (IOException e1) {
			// e1.printStackTrace();
			System.out.println("IO异常"+e1);
		}
		finally{
			try {
				ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				System.out.println("延时出错！");
			}
		}
		
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
		}finally{
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

}
