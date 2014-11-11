package com.baidu.grab;

import com.baidu.grab.core.ActorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class GrabMain {
    public static ApplicationContext applicationContext;
    public static final Logger log = LoggerFactory.getLogger(GrabMain
            .class);

    public static void main(String[] args) throws Exception {
        log.info("启动应用程序");
        applicationContext = new ClassPathXmlApplicationContext("mongo.xml","quartz.xml");
        ActorManager manager = new ActorManager();
        manager.startApplication();
    }

}
