package com.cui.bean;

import java.net.URI;

/**
 * Created by jarvis on 2016/10/29.
 * 图片实例
 */
public class Image {
    String imageName;
    URI photourl;
    String folderPath;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public Image(String imageName, URI photourl, String folderPath) {
        super();
        this.imageName = imageName;
        this.photourl = photourl;
        this.folderPath = folderPath;
    }

    public Image() {
        super();
    }

}
