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
        //分类页右侧地址
        public static final String WARES_LIST = BASE_URL + "wares/list";
        //首页商品列表点击进入的--页面地址
        public static final String WARES_CAMPAIN_LIST = BASE_URL + "wares/campaign/list";
        //热卖商品列表点击进入的--H5页面地址
        public static final String WARES_DETAIL = BASE_URL + "wares/detail.html";
        //登录
        public static final String LOGIN = BASE_URL + "auth/login";
        //受保护的地址，可验证token
        public static final String USER_DETAILS = BASE_URL + "user/get?id=1";
        //注册
        public static final String REGISTER = BASE_URL + "auth/reg";
        //提交订单
        public static final String ORDER_CREATE = BASE_URL + "/order/create";
        //支付完成
        public static final String ORDER_COMPLEPE = BASE_URL + "/order/complete";
        //我的订单页面
        public static final String ORDER_LIST = BASE_URL + "order/list";
        //收货地址列表
        public static final String ADDRESS_LIST = BASE_URL + "addr/list";
        //新增收货地址
        public static final String ADDRESS_CREATE = BASE_URL + "addr/create";
        //更新收货地址
        public static final String ADDRESS_UPDATE = BASE_URL + "addr/update";
        //删除收货地址
        public static final String ADDRESS_DELETE = BASE_URL + "addr/del";
        //加入收藏夹
        public static final String FAVORITE_CREATE = BASE_URL + "favorite/create";
        //收藏列表
        public static final String FAVORITE_LIST = BASE_URL + "favorite/list";
        //删除收藏夹
        public static final String FAVORITE_DEL = BASE_URL + "favorite/del";
    }

}
