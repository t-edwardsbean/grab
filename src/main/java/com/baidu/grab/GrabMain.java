package com.baidu.grab;

import com.baidu.grab.core.ActorManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class GrabMain {
    public static ApplicationContext applicationContext;
    public static final Logger log = LoggerFactory.getLogger(GrabMain
            .class);

    public static void main(String[] args) throws Exception {
        log.info("启动应用程序");
        String address = InetAddress.getLocalHost().getHostAddress();
        MDC.put("host", address);
        MDC.put("source", "grabApp");
        Properties p = new Properties();
        p.load(GrabMain.class.getResourceAsStream("/log4j.properties"));
        PropertyConfigurator.configure(p);
        System.setProperty("org.terracotta.quartz.skipUpdateCheck","true");
        applicationContext = new ClassPathXmlApplicationContext("mongo.xml","quartz.xml","dict.xml");
        ActorManager manager = new ActorManager();
        manager.startApplication();
    }

}
