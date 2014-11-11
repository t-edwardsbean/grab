package com.baidu.schedule;


import com.baidu.grab.GrabSupervisor;

/**
 * 每小时调度抓取数据
 * Created by edwardsbean on 14-11-7.
 */
public class HourJob {
    public void grab() {
        GrabSupervisor.startGrabCity();
        GrabSupervisor.startGrabStation();
    }

}
