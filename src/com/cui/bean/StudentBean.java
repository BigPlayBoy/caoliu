package com.cui.bean;

import java.sql.Blob;
import java.util.Date;

public class StudentBean {
	private int sid;// 学号
	private String sname;// 姓名
	private String gender;// 性别
	private Date birthday;// 出生日期
	private Blob picture;// 照片

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}



	public StudentBean(int sid, String sname, String gender, Date birthday, Blob picture) {
		this.sid = sid;
		this.sname = sname;
		this.gender = gender;
		this.birthday = birthday;
		this.picture = picture;
	}

	public StudentBean() {
	}

	@Override
	public String toString() {
		return "StudentBean [sid=" + sid + ", sname=" + sname + ", gender=" + gender + ", birthday=" + birthday
				+ ", picture=" + picture + "]";
	}

}
