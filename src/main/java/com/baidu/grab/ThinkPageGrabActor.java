package com.baidu.grab;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.baidu.exception.ApiException;
import com.baidu.exception.HttpException;
import com.baidu.message.Grab;
import com.baidu.model.ThinkPageAirMsg;
import com.baidu.model.ThinkPageWeather;
import com.baidu.mongo.MongoPM25City;
import com.baidu.tools.ThinkPageGrab;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.restart;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageGrabActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private static ActorRef mongoActor;
    private static String key;
    private Gson gson = new Gson();

    private static SupervisorStrategy strategy = new OneForOneStrategy(-1,
            Duration.create("10 second"), new Function<Throwable, SupervisorStrategy.Directive>() {
        public SupervisorStrategy.Directive apply(Throwable t) {
            //如果mongoActor出错，则重启它
            return restart();
        }
    });

    @Override
    public void preStart() throws Exception {
        mongoActor = getContext().actorOf(Props.create(MongoActor.class).withMailbox("custom-mailbox"),
                "pm25MongoActor");
        /**
         * TODO 时间校验
         */
        ActorSystem system = getContext().system();
        system.settings().config().getString("pm25-Key");
        system.scheduler().schedule(
                Duration.create(1, TimeUnit.SECONDS),
                Duration.create(3, TimeUnit.SECONDS),
                getSelf(), new Grab(), system.dispatcher(), null);
    }

    //重写postRestart,防止preStart又一次进行初始化
    @Override
    public void postRestart(Throwable reason) throws Exception {
    }

    //重写preRestart，防止mongoActor受到影响
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.error(reason,"Restarting due to [{}] when processing [{}]",reason.getMessage());
        Grab grab = new Grab();
        //访问外网失败，重新访问
        getSelf().tell(grab, ActorRef.noSender());
        postStop();
    }


    @Override
    public void onReceive(Object message) throws Exception {
        log.info("收到Grab事件，开始抓取");
        if (message instanceof Grab) {
            try {
                /**
                 * TODO
                 * 获取全部城市，采集点的数据
                 */
                String json = ThinkPageGrab.getData();
                log.info("Api调用结果：" + json);
                //格式化数据
                ThinkPageAirMsg thinkPageAirMsg = gson.fromJson(json, ThinkPageAirMsg.class);
                if (!"OK".equals(thinkPageAirMsg.getStatus())) {
                    throw new ApiException("Api返回非天气数据");
                }
                //过滤字段，封装成MongoPM25City
                ThinkPageWeather weather = thinkPageAirMsg.getWeather().get(0);
                MongoPM25City city = new MongoPM25City();
                city.setCity(weather.getCity_name());
                city.setPm25(weather.getAir_quality().getCity().getPm25());
                //时间需要处理
                city.setLast_update(weather.getAir_quality().getCity().getLast_update());
                mongoActor.tell(city, getSelf());
            } catch (ApiException e) {
                throw e;
            } catch (JsonSyntaxException e) {
                throw new ApiException("Api返回非天气数据", e);
            } catch (Exception e) {
                throw new HttpException("无法访问数据源", e);
            }

        }
    }
}