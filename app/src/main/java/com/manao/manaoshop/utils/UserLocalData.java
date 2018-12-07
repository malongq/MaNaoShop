package com.manao.manaoshop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.bean.User;

/**
 * 保存和清除用户信息
 */
public class UserLocalData {

    public static void putUser(Context context, User user) {
        String user_json = JSONUtils.toJSON(user);
        SPreferencesUtils.putString(context, Constants.USER_JSON, user_json);
    }

    public static void putToken(Context context, String token) {
        SPreferencesUtils.putString(context, Constants.TOKEN, token);
    }

    public static User getUser(Context context) {
        String user_json = SPreferencesUtils.getString(context, Constants.USER_JSON);
        if (!TextUtils.isEmpty(user_json)) {
            return JSONUtils.fromJson(user_json, User.class);
        }
        return null;
    }

    public static String getToken(Context context) {
        return SPreferencesUtils.getString(context, Constants.TOKEN);

    }

    public static void clearUser(Context context) {
        SPreferencesUtils.putString(context, Constants.USER_JSON, "");

    }

    public static void clearToken(Context context) {
        SPreferencesUtils.putString(context, Constants.TOKEN, "");
    }

}
