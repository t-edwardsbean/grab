package com.baidu;

import com.baidu.grab.PM25Grab;
import com.baidu.grab.ThinkPageGrab;
import com.baidu.model.*;
import com.baidu.mongo.MongoPM25City;
import com.baidu.mongo.MongoPM25Station;
import com.baidu.mongo.MongoThinkPageCity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-5.
 */
@ComponentScan
@EnableAutoConfiguration
public class GrabMain implements CommandLineRunner {
    @Autowired
    MongoTemplate mongoTemplate;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GrabMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        String thinkPageJson = ThinkPageGrab.getData();
        Gson gson = new Gson();
        ThinkPageAirMsg thinkPageAirMsg = gson.fromJson(thinkPageJson, ThinkPageAirMsg.class);
        MongoThinkPageCity thinkPageCity = new MongoThinkPageCity();
        //是否支持批量获取
        ThinkPageWeather weather = thinkPageAirMsg.getWeather().get(0);
        thinkPageCity.setCity(weather.getCity_name());
        thinkPageCity.setPm10(weather.getAir_quality().getCity().getPm10());
        thinkPageCity.setPm25(weather.getAir_quality().getCity().getPm25());
        //时间需要处理
        thinkPageCity.setLast_update(weather.getAir_quality().getCity().getLast_update());
        mongoTemplate.save(thinkPageCity);

        String pm25Json = PM25Grab.getStationData("福州");
        List<PM25Msg> pm25Msgs = gson.fromJson(pm25Json, new TypeToken<List<PM25Msg>>() {
        }.getType());
        for (PM25Msg pm25Msg : pm25Msgs) {
            MongoPM25Station pm25Station = new MongoPM25Station();
            pm25Station.setCity(pm25Msg.getArea());
            //时间需要处理
            pm25Station.setLast_update(pm25Msg.getTime_point());
            pm25Station.setPm25_24h(pm25Msg.getPm2_5_24h());
            pm25Station.setPm25(pm25Msg.getPm2_5());
            pm25Station.setStation(pm25Msg.getPosition_name());
            mongoTemplate.save(pm25Msgs);
        }

        pm25Json = PM25Grab.getCityData("福州");
        pm25Msgs = gson.fromJson(pm25Json, new TypeToken<List<PM25Msg>>(){}.getType());
        for (PM25Msg pm25Msg : pm25Msgs) {
            MongoPM25City pm25Station = new MongoPM25City();
            pm25Station.setCity(pm25Msg.getArea());
            //时间需要处理
            pm25Station.setLast_update(pm25Msg.getTime_point());
            pm25Station.setPm25_24h(pm25Msg.getPm2_5_24h());
            pm25Station.setPm25(pm25Msg.getPm2_5());
            mongoTemplate.save(pm25Msgs);
        }
    }
}
