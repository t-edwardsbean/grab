package com.baidu;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.baidu.grab.GrabSupervisor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class GrabMain{
    public static ApplicationContext applicationContext;
    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("mongo.xml");
        ActorSystem system = ActorSystem.create("grabSystem");
        system.actorOf(Props.create(GrabSupervisor.class),
                "supervisor");
        system.awaitTermination();
    }

}
