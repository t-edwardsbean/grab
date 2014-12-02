package com.baidu.grab.schedule;


import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import com.baidu.grab.core.ActorManager;
import com.baidu.grab.core.GrabSupervisor;
import com.baidu.grab.message.GrabCity;
import com.baidu.grab.message.GrabStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每小时调度抓取数据
 * Created by edwardsbean on 14-11-7.
 */
public class PM25HourJob {
    public static final Logger log = LoggerFactory.getLogger(PM25HourJob.class);

    public void grab() {
        try {
            log.info("定时调度,发送GrabCity,GrabStation事件");
            ActorSelection pm25Actor = ActorManager.system.actorSelection("akka://grabSystem/user/supervisor/pm25GrabActor");
            pm25Actor.tell(new GrabCity(), ActorRef.noSender());
            pm25Actor.tell(new GrabStation(), ActorRef.noSender());
        } catch (Exception e) {
            log.warn("actorSelect异常",e);
        }
    }

}
