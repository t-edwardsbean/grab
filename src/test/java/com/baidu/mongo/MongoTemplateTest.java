package com.baidu.mongo;

import com.baidu.model.ThinkPageAirMsg;
import com.baidu.model.ThinkPageWeather;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:mongo.xml"})
public class MongoTemplateTest {
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testName() throws Exception {
        Gson gson = new Gson();
        String json = "{\"status\":\"OK\",\"weather\":[{\"city_name\":\"北京\",\"city_id\":\"CHBJ000000\",\"last_update\":\"2014-11-05T15:07:56+08:00\",\"air_quality\":{\"city\":{\"aqi\":\"33\",\"pm25\":\"11\",\"pm10\":\"32\",\"so2\":\"6\",\"no2\":\"14\",\"co\":\"0.283\",\"o3\":\"65\",\"quality\":\"优\",\"last_update\":\"2014-11-05T13:00:00+08:00\"},\"stations\":null}}]}";
        ThinkPageAirMsg thinkPageAirMsg = gson.fromJson(json, ThinkPageAirMsg.class);
        List<ThinkPageWeather> weatherList = thinkPageAirMsg.getWeather();
        for (ThinkPageWeather weather : weatherList) {
            mongoTemplate.save(weather);
        }
    }
}
