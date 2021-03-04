package com.getrent.smartsecurity.helper;

import com.getrent.smartsecurity.modelClass.ImageList;

import java.util.ArrayList;
import java.util.List;

public class Apis {

    public static String imgList = "http://getrentbd.com/rasPi/imageSendGet.php";
    public static String imgDelet = "http://getrentbd.com//rasPi/imageDelete.php?";
    public static String settingPost = "http://getrentbd.com//rasPi/settings.php";
    public static String settingGet = "http://getrentbd.com//rasPi/settingGet.php";
    public static String baseUrl="";
    public static List<ImageList> imageList =  new ArrayList<>();
}
