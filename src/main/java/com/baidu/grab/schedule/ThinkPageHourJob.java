package com.baidu.grab.schedule;

import com.baidu.grab.GrabMain;
import com.baidu.grab.core.GrabSupervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-11.
 */
public class ThinkPageHourJob {
    public static final Logger log = LoggerFactory.getLogger(ThinkPageHourJob.class);

    public void grab() {
        log.info("定时调度，向ThinkPageGrabActor发送Grab事件");
        CityDict cityDict = (CityDict)GrabMain.applicationContext.getBean("cityDict");
        List<String> cities = cityDict.getAllCity();
        for (String city : cities) {
            GrabSupervisor.grabCity(city);
        }
    }
}
