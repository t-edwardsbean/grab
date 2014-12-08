package com.baidu.grab.tools;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class PM25Grab {

    public static String getAllCityData(String key) throws Exception {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse response = httpUtil.doGet("http://www.pm25.in/api/querys/aqi_ranking.json?token=" + key, null);
        String result = EntityUtils.toString(response.getEntity());
        httpUtil.releaseConnection();
        return result;
    }

    public static String getAllStationData(String key) throws Exception {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse response = httpUtil.doGet("http://www.pm25.in/api/querys/all_cities.json?token=" + key, null);
        String result = EntityUtils.toString(response.getEntity());
        httpUtil.releaseConnection();
        return result;
    }
}
