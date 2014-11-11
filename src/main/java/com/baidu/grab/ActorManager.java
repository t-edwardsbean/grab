package com.baidu.grab;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 *
 * Created by edwardsbean on 14-11-7.
 */
public class ActorManager {
    public static ActorSystem system;
    public void startApplication() {
        Config config = ConfigFactory.load();
        system = ActorSystem.create("grabSystem", config);
        system.actorOf(Props.create(GrabSupervisor.class),
                "supervisor");
        system.awaitTermination();
    }
}
