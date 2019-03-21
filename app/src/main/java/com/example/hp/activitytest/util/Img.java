package com.example.hp.activitytest.util;

public class Img {
    private static String [] imgs ={
            "http://wonder.vipgz1.idcfengye.com/ddd/image/001-man-13.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/030-boy-3.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/024-man-2.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/018-man-8.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/036-woman.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/035-woman-1.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/034-woman-2.png",
            "http://wonder.vipgz1.idcfengye.com/ddd/image/011-woman-5.png",

    };

    public static String getImg(int i) {

        return imgs[i%8];
    }


}
