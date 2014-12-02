package com.baidu.grab.core;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.baidu.grab.exception.ApiException;
import com.baidu.grab.exception.HttpException;
import com.baidu.grab.message.Grab;
import com.baidu.grab.message.GrabCity;
import com.baidu.grab.message.GrabStation;
import scala.Option;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;

/**
 * 监控GrabActor
 * Created by edwardsbean on 14-11-5.
 */
public class GrabSupervisor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public static ActorRef pm25GrabActor;
    public static ActorRef thinkPageGrabActor;

    public GrabSupervisor() {
        pm25GrabActor = getContext().actorOf(Props.create(PM25GrabActor.class),
                "pm25GrabActor");

//        thinkPageGrabActor = getContext().actorOf(Props.create(ThinkPageGrabActor.class),
//                "thinkPageGrabActor");
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(-1,
            Duration.create("10 second"), new Function<Throwable, SupervisorStrategy.Directive>() {
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof ApiException) {
                /*
                   如果数据源Api返回非正常格式数据:
                   1：数据源服务端更改了城市名，比如thinkpage将胶南改成了黄岛
                   2：Api调用次数超过数据源服务端限制
                   3：其他服务端数据异常
                   则忽略该次调用,并告警
                 */
                return resume();
            } else {
                //如果GrabActor访问外网出错，则重启它,并重试抓取
                return restart();
            }
        }
    });

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }

    public static void grabCity(String city) {
        thinkPageGrabActor.tell(new Grab(city), ActorRef.noSender());
    }

/*    public static void startGrabCity() {
        pm25GrabActor.tell(new GrabCity(), ActorRef.noSender());
    }

    public static void startGrabStation() {
        pm25GrabActor.tell(new GrabStation(), ActorRef.noSender());
    }*/
}
