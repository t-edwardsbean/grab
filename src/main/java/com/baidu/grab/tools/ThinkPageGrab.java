package com.baidu.grab.tools;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageGrab {
    public static String getData() throws Exception {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse response = httpUtil.doGet("https://api.thinkpage.cn/v2/weather/air.json?city=CHBJ000000&language=zh-chs&unit=c&aqi=city&key=EMVRPYXERN", null);
        return  EntityUtils.toString(response.getEntity());
    }
}
