package com.baidu;

import com.baidu.grab.ActorManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class GrabMain {
    public static ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("mongo.xml","quartz.xml");
        ActorManager manager = new ActorManager();
        manager.startApplication();
    }

}
