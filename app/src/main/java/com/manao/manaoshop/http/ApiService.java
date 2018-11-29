package com.manao.manaoshop.http;

/**
 * Created by Malong
 * on 18/11/13.
 * url 管理类
 */
public class ApiService {

    public static class API {

        //基本url
        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";

        //首页广告图下面数据的地址
        public static final String HOME_RECRCLER_URL = BASE_URL + "campaign/recommend";
        //热卖页地址
        public static final String WARES_HOT = BASE_URL + "wares/hot";
        //分类页左侧列表地址
        public static final String CATEGROY_LEFT = BASE_URL + "category/list";
        //分类页Banner地址
        public static final String BANNER = BASE_URL + "banner/query";
        //分类页Banner地址
        public static final String WARES_LIST = BASE_URL + "wares/list";
    }

}
