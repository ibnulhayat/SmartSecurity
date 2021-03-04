package com.getrent.smartsecurity.modelClass;

public class ImageList {

    private String id;
    private String dateTime;
    private String imgUrl;

    public ImageList(String id, String dateTime, String imgUrl) {
        this.id = id;
        this.dateTime = dateTime;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
