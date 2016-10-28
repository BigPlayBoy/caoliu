package com.cui.bean;

//照片的属性
public class PhotoBean {
	String photoname;
	String photoPath;
	public String getPhotoname() {
		return photoname;
	}
	public void setPhotoname(String photoname) {
		this.photoname = photoname;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	@Override
	public String toString() {
		return "PhooBean [photoname=" + photoname + ", photoPath=" + photoPath + "]";
	}
	
}
