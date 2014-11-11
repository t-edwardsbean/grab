package com.baidu.mongo;

import com.baidu.grab.model.PM25Msg;
import com.baidu.grab.model.ThinkPageAirMsg;
import com.baidu.grab.schedule.CityDict;
import com.baidu.grab.tools.PM25Grab;
import com.baidu.grab.tools.ThinkPageGrab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by edwardsbean on 14-11-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:dict.xml"})
public class TimeTest {
    @Autowired
    private CityDict cityDict;
    public static final Logger log = LoggerFactory.getLogger(TimeTest
            .class);

    /**
     * 测试结果显示，thinkpage数据延迟一个小时，每小时更新一次。
     * 如：2:00~2:59，获取到的数据都是1:00的
     * @throws Exception
     */
    @Test
    public void testTime() throws Exception {
        java.util.TimeZone zone = java.util.TimeZone.getTimeZone("GMT-8:00");
        while(true) {
            System.out.println("睡眠");
            Thread.sleep(TimeUnit.MINUTES.toMillis(1));
            String json = ThinkPageGrab.getData("北京","EMVRPYXERN");
            Gson gson = new Gson();
            ThinkPageAirMsg thinkPageAirMsg = gson.fromJson(json, ThinkPageAirMsg.class);
            String cityTime = thinkPageAirMsg.getWeather().get(0).getLast_update();
            String airTime = thinkPageAirMsg.getWeather().get(0).getAir_quality().getCity().getLast_update();
            java.util.Calendar cal = java.util.Calendar.getInstance(zone);
//            if (!cityTime.equals("2014-11-11T13:28:19+08:00")) {
//                System.out.println("当前时间：" + cal.getTime());
//                System.out.println("抓取到的City更新时间" + cityTime);
//            }
            if (! airTime.equals("2014-11-11T13:00:00+08:00")) {
                System.out.println("当前时间：" + cal.getTime());
                System.out.println("抓取到的Air更新时间" + airTime);
            }
        }

    }

    /**
     * 测试结果显示，pm25.in获取114个城市空气质量，实际上是返回了190个，
     * 即所有城市
     * @throws Exception
     */
    @Test
    public void testRestCity() throws Exception {
        Gson gson = new Gson();
        String cityJson = PM25Grab.getAllCityData("szE3xqfafJHv7GnDbL57");
        List<PM25Msg> pm25CityMsgs = gson.fromJson(cityJson, new TypeToken<List<PM25Msg>>() {
        }.getType());
        List<String> cities = cityDict.getAllCity();
        int i=0;
        for (PM25Msg pm25Msg : pm25CityMsgs) {
            String city = pm25Msg.getArea();
            if(cities.contains(city)) {
                log.info("存在该city:" + city);
                cities.remove(city);
            }
            i ++;
        }
        for (String city:cities) {
            System.out.println(city);
        }
        System.out.println("api获取总城市：" + i);
    }
}
