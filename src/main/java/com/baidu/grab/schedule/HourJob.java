package com.baidu.grab.schedule;


import com.baidu.grab.core.GrabSupervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每小时调度抓取数据
 * Created by edwardsbean on 14-11-7.
 */
public class HourJob {
    public static final Logger log = LoggerFactory.getLogger(HourJob.class);

    public void grab() {
        log.debug("定时调度，发送GrabCity,GrabStation事件");
        GrabSupervisor.startGrabCity();
        GrabSupervisor.startGrabStation();
    }

}
