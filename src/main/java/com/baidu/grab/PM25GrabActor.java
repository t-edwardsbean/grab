package com.baidu.grab;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.baidu.exception.ApiException;
import com.baidu.exception.HttpException;
import com.baidu.message.Grab;
import com.baidu.message.GrabCity;
import com.baidu.message.GrabStation;
import com.baidu.model.PM25Msg;
import com.baidu.mongo.MongoPM25City;
import com.baidu.mongo.MongoPM25Station;
import com.baidu.tools.PM25Grab;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.List;
import static akka.actor.SupervisorStrategy.restart;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class PM25GrabActor extends UntypedActor {
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
        log.info("PM25GrabActor preStart");
        mongoActor = getContext().actorOf(Props.create(MongoActor.class).withMailbox("custom-mailbox"),
                "pm25MongoActor");
        /**
         * TODO 时间校验
         */
        ActorSystem system = getContext().system();
        key = system.settings().config().getString("pm25-key");
    }

    //重写postRestart,防止preStart又一次进行初始化
    @Override
    public void postRestart(Throwable reason) throws Exception {
    }

    //重写preRestart，防止mongoActor受到影响
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.error(reason, "Restarting due to [{}] when processing [{}]", reason.getMessage());
        Grab grab = new Grab();
        //访问外网失败，重新访问
        getSelf().tell(grab, ActorRef.noSender());
        postStop();
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GrabCity) {
            log.info("收到GrabCity事件，开始抓取");
            try {
                String cityJson = PM25Grab.getAllCityData(key);
                List<PM25Msg> pm25CityMsgs = gson.fromJson(cityJson, new TypeToken<List<PM25Msg>>() {
                }.getType());
                //过滤字段，封装成MongoPM25City
                for (PM25Msg pm25Msg : pm25CityMsgs) {
                    MongoPM25City city = new MongoPM25City();
                    city.setCity(pm25Msg.getArea());
                    //时间需要处理
                    city.setLast_update(pm25Msg.getTime_point());
                    city.setPm25_24h(pm25Msg.getPm2_5_24h());
                    city.setPm25(pm25Msg.getPm2_5());
                    mongoActor.tell(city, getSelf());
                }

            } catch (JsonSyntaxException e) {
                throw new ApiException("Api返回非天气数据", e);
            } catch (Exception e) {
                throw new HttpException("无法访问数据源", e);
            }

        } else if (message instanceof GrabStation) {
            try {
                log.info("收到GrabStation事件，开始抓取");
                String stationJson = PM25Grab.getAllStationData(key);
                List<PM25Msg> pm25StationMsgs = gson.fromJson(stationJson, new TypeToken<List<PM25Msg>>() {
                }.getType());
                //过滤字段，封装成MongoPM25City
                for (PM25Msg pm25Msg : pm25StationMsgs) {
                    MongoPM25Station station = new MongoPM25Station();
                    station.setCity(pm25Msg.getArea());
                    //时间需要处理
                    station.setLast_update(pm25Msg.getTime_point());
                    station.setPm25_24h(pm25Msg.getPm2_5_24h());
                    station.setPm25(pm25Msg.getPm2_5());
                    station.setStation(pm25Msg.getPosition_name());
                    mongoActor.tell(station, getSelf());
                }
            } catch (JsonSyntaxException e) {
                throw new ApiException("Api返回非天气数据", e);
            } catch (Exception e) {
                throw new HttpException("无法访问数据源", e);
            }
        }
    }
}