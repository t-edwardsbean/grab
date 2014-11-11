package com.baidu.grab.core;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.baidu.grab.message.GrabCity;
import com.baidu.grab.message.GrabStation;
import scala.Option;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.restart;

/**
 * 监控GrabActor
 * Created by edwardsbean on 14-11-5.
 */
public class GrabSupervisor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public static ActorRef pm25GrabActor;
    public ActorRef thinkPageGrabActor;

    public GrabSupervisor() {
        pm25GrabActor = getContext().actorOf(Props.create(PM25GrabActor.class),
                "pm25GrabActor");

//        thinkPageGrabActor = getContext().actorOf(Props.create(ThinkPageGrabActor.class),
//                "thinkPageGrabActor");
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(-1,
            Duration.create("10 second"), new Function<Throwable, SupervisorStrategy.Directive>() {
        public SupervisorStrategy.Directive apply(Throwable t) {
            //如果GrabActor访问外网出错，则重启它
            return restart();
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
        postStop();
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }

    public static void startGrabCity() {
        pm25GrabActor.tell(new GrabCity(), ActorRef.noSender());
    }

    public static void startGrabStation() {
        pm25GrabActor.tell(new GrabStation(), ActorRef.noSender());
    }
}
