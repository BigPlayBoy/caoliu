package com.cui.bean;

import java.util.Date;

public class PageBean {
	// 1.公有的类
	// 2.提供公有的不带参数的默认的构造方法
	// 3.属性私有
	// 4.属性setter/getter封装

	// 网页要有网页标题和路径
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String title;
	private String url_md5;
	private String Page_URL;
	private String create_date ;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl_md5() {
		return url_md5;
	}

	public void setUrl_md5(String url_md5) {
		this.url_md5 = url_md5;
	}

	public String getPage_URL() {
		return Page_URL;
	}

	public void setPage_URL(String page_URL) {
		Page_URL = page_URL;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	

	public PageBean(int id, String title, String url_md5, String page_URL, String create_date) {
		super();
		this.id = id;
		this.title = title;
		this.url_md5 = url_md5;
		Page_URL = page_URL;
		this.create_date = create_date;
	}

	public PageBean() {
	}

	@Override
	public String toString() {
		return "PageBean [id=" + id + ", title=" + title + ", url_md5=" + url_md5 + ", Page_URL=" + Page_URL
				+ ", create_date=" + create_date + "]";
	}

	

}
