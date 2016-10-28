package com.cui.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DownloadUtils implements Runnable {
	String pathname;
	String photoname;
	String url;

	public String getPathname() {
		return pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	public String getPhotoname() {
		return photoname;
	}

	public void setPhotoname(String photoname) {
		this.photoname = photoname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DownloadUtils(String pathname, String photoname, String url) {
		super();
		this.pathname = pathname;
		this.photoname = photoname;
		this.url = url;
	}

	@Override
	public void run() {
		DownloadUtils.downloadPhoto(this.pathname, this.photoname, this.url);
	}

	public static boolean fun1() {
		for (int i = 0; i < 100; i++) {
			System.out.println("当前fun1输出的值：" + i);
		}
		return false;
	}

	/**
	 *
	 * @param pathname
	 * @param photoname
	 * @param url
	 * @return
	 */
	public static boolean downloadPhoto(String pathname, String photoname, String url) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		FileOutputStream fos = null;
		// BufferedWriter bufferw=new BufferedWriter(new
		// OutputStreamWriter(fos));
		InputStream inputStream = null;
		// BufferedReader br=new BufferedReader(new
		// InputStreamReader(inputStream));
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
			byte[] data = new byte[2 * 1024];
			int len = 0;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				System.out.println("延时出错！");
			}
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
				fos.flush();
			}
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("在downloadPhoto中IO异常！" + e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (fos != null) {
					fos.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
