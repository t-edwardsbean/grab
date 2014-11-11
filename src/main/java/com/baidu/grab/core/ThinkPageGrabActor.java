package com.baidu.grab.core;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.baidu.grab.exception.ApiException;
import com.baidu.grab.exception.HttpException;
import com.baidu.grab.message.Grab;
import com.baidu.grab.model.ThinkPageAirMsg;
import com.baidu.grab.model.ThinkPageStation;
import com.baidu.grab.model.ThinkPageWeather;
import com.baidu.grab.mongo.MongoPM25City;
import com.baidu.grab.mongo.MongoPM25Station;
import com.baidu.grab.tools.ThinkPageGrab;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.List;
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
        log.info("ThinkPageGrabActor preStart");
        mongoActor = getContext().actorOf(Props.create(MongoActor.class).withMailbox("custom-mailbox"),
                "tpMongoActor");
        /**
         * TODO 时间校验
         */
        ActorSystem system = getContext().system();
        key = system.settings().config().getString("tp-key");
    }

    //重写postRestart,防止preStart又一次进行初始化
    @Override
    public void postRestart(Throwable reason) throws Exception {
    }

    //重写preRestart，防止mongoActor受到影响
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.error(reason, "ThinkPageActor Restarting due to [{}] when processing [{}]", reason.getMessage());
        //访问外网失败，重新访问
        getSelf().tell(message.get(), ActorRef.noSender());
    }


    @Override
    public void onReceive(Object message) throws Exception {
        log.debug("收到Grab事件，开始抓取");
        if (message instanceof Grab) {
            try {
                Grab grab = (Grab) message;
                String json = ThinkPageGrab.getData(grab.getCity(), key);
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
                /**
                 * TODO 时间格式转换，更新时间如果改变则告警
                 */
                city.setLast_update(weather.getAir_quality().getCity().getLast_update());
                mongoActor.tell(city, getSelf());

                //过滤字段，封装成MongoPM25Station
                List<ThinkPageStation> stations = weather.getAir_quality().getStations();
                for (ThinkPageStation station : stations) {
                    MongoPM25Station mongoPM25Station = new MongoPM25Station();
                    mongoPM25Station.setCity(city.getCity());
                    /**
                     * TODO 时间格式转换，更新时间如果改变则告警
                     */
                    mongoPM25Station.setLast_update(station.getLast_update());
                    mongoPM25Station.setPm25(station.getPm25());
                    mongoPM25Station.setStation(station.getStation());
                }
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