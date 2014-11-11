package com.baidu.grab.core;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by edwardsbean on 14-11-7.
 */
public class ActorManager {
    public static final Logger log = LoggerFactory.getLogger(ActorManager
            .class);
    public static ActorSystem system;
    public void startApplication() {
        log.info("加载Actor配置文件");
        Config config = ConfigFactory.load();
        system = ActorSystem.create("grabSystem", config);
        system.actorOf(Props.create(GrabSupervisor.class),
                "supervisor");
    }
}
