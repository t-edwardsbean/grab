package com.baidu.tools;

import com.baidu.tools.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class PM25Grab {
    public static String getCityData(String city) throws Exception {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse response = httpUtil.doGet("http://www.pm25.in/api/querys/pm2_5.json?city=" + city + "&stations=no&token=5j1znBVAsnSf5xQyNQyq", null);
        return EntityUtils.toString(response.getEntity());

    }

    public static String getStationData(String city) throws Exception {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse response = httpUtil.doGet("http://www.pm25.in/api/querys/pm2_5.json?city=" + city + "&stations=yes&token=5j1znBVAsnSf5xQyNQyq", null);
        return EntityUtils.toString(response.getEntity());

    }
}
