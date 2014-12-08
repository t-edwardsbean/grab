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
import com.baidu.grab.model.PM25Msg;
import com.baidu.grab.mongo.MongoPM25City;
import com.baidu.grab.mongo.MongoPM25Station;
import com.baidu.grab.tools.PM25Grab;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.restart;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import scala.runtime.AbstractFunction0;

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
    },false);

    @Override
    public void preStart() throws Exception {
        log.info("PM25GrabActor preStart");
        mongoActor = getContext().actorOf(Props.create(MongoActor.class).withMailbox("custom-mailbox"),
                "pm25MongoActor");
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
        Object m = message.getOrElse(new AbstractFunction0<Object>(){
            @Override
            public Object apply() {
                return "空消息";
            }
        });
        log.warning("PMActor Restarting due to [{}] when processing [{}]", reason.getMessage(),m.toString());
        //访问外网失败，延迟3s,重新访问
        ActorSystem system = getContext().system();
        system.scheduler().scheduleOnce(Duration.apply(3, TimeUnit.SECONDS),
                getSelf(), m, system.dispatcher(), ActorRef.noSender());
    }


    private String parseTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat newDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date newTime;
        try {
            newTime = df.parse(time);
        } catch (ParseException e) {
            log.warning("格式化时间出错",e);
            return  time;
        }
        return newDf.format(newTime);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GrabCity) {
            log.info("收到GrabCity事件，开始抓取");
            String cityJson = "";
            try {
                cityJson = PM25Grab.getAllCityData(key);
                log.debug("抓取完毕，准备格式化");
                List<PM25Msg> pm25CityMsgs = gson.fromJson(cityJson, new TypeToken<List<PM25Msg>>() {
                }.getType());
                //过滤字段，封装成MongoPM25City
                for (PM25Msg pm25Msg : pm25CityMsgs) {
                    MongoPM25City city = new MongoPM25City();
                    city.setCity(pm25Msg.getArea());
                    //统一做格式转换
                    city.setLast_update(parseTime(pm25Msg.getTime_point()));
                    city.setPm25_24h(pm25Msg.getPm2_5_24h());
                    city.setPm25(pm25Msg.getPm2_5());
                    mongoActor.tell(city, getSelf());
                }

            } catch (JsonSyntaxException e) {
                throw new ApiException("GrabCity Api返回非天气数据:" + cityJson, e);
            } catch (Exception e) {
                throw new HttpException("GrabCity Api无法访问数据源", e);
            }

        } else if (message instanceof GrabStation) {
            String stationJson = "";
            try {
                log.info("收到GrabStation事件，开始抓取");
                stationJson = PM25Grab.getAllStationData(key);
                log.debug("抓取完毕，准备格式化");
                List<PM25Msg> pm25StationMsgs = gson.fromJson(stationJson, new TypeToken<List<PM25Msg>>() {
                }.getType());
                //过滤字段，封装成MongoPM25Station
                for (PM25Msg pm25Msg : pm25StationMsgs) {
                    MongoPM25Station station = new MongoPM25Station();
                    station.setCity(pm25Msg.getArea());
                    //统一做时间转换
                    station.setLast_update(parseTime(pm25Msg.getTime_point()));
                    station.setPm25_24h(pm25Msg.getPm2_5_24h());
                    station.setPm25(pm25Msg.getPm2_5());
                    station.setStation(pm25Msg.getPosition_name());
                    mongoActor.tell(station, getSelf());
                }
            } catch (JsonSyntaxException e) {
                throw new ApiException("GrabStation Api返回非天气数据:" + stationJson, e);
            } catch (Exception e) {
                throw new HttpException("GrabStation Api无法访问数据源", e);
            }
        } else {
            unhandled(message);
        }
    }
}