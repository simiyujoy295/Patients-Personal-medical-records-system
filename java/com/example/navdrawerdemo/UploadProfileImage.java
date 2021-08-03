package com.example.navdrawerdemo;

public class UploadProfileImage {
    private String imgName;
    private String imgURI;
    public UploadProfileImage(){
    }

    public UploadProfileImage(String imgName, String imgURI) {
        if(imgName.trim().equals("")){
            imgName = "Untitled";
        }

        this.imgName = imgName;
        this.imgURI = imgURI;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgURI() {
        return imgURI;
    }

    public void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }
}


