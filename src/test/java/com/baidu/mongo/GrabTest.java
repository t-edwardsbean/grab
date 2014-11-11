package com.baidu.mongo;

import com.baidu.grab.model.ThinkPageAirMsg;
import com.baidu.grab.schedule.CityDict;
import com.baidu.grab.tools.ThinkPageGrab;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:dict.xml"})
public class GrabTest {
    @Autowired
    CityDict cityDict;

    /**
     * 测试字典中的城市，是否能查询到
     */
    @Test
    public void testDict() {
        List<String> cities = cityDict.getAllCity();
        Gson gson = new Gson();
        for (String city : cities) {
            String json = null;
            try {
                json = ThinkPageGrab.getData(city, "EMVRPYXERN");
                ThinkPageAirMsg thinkPageAirMsg = gson.fromJson(json, ThinkPageAirMsg.class);
                if (!"OK".equals(thinkPageAirMsg.getStatus())) {
                    System.out.println(city);
                }
            } catch (Exception e) {
                System.out.println(city);
            }

        }
    }
}
