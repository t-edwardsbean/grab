package com.baidu;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.baidu.grab.GrabSupervisor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class GrabMain {
    public static ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("mongo.xml");
        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create("grabSystem", config);
        system.actorOf(Props.create(GrabSupervisor.class),
                "supervisor");
        system.awaitTermination();
    }

}
