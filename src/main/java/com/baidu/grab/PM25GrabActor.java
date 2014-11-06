package com.baidu.grab;

import akka.actor.*;
import akka.japi.Function;
import com.baidu.exception.ApiException;
import com.baidu.exception.HttpException;
import com.baidu.message.Grab;
import com.baidu.model.PM25Msg;
import com.baidu.mongo.MongoPM25City;
import com.baidu.tools.PM25Grab;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class PM25GrabActor extends UntypedActor {
    private static ActorRef mongoActor;
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
        mongoActor = getContext().actorOf(Props.create(MongoActor.class),
                "pm25MongoActor");
        /**
         * TODO 时间校验
         */
        ActorSystem system = getContext().system();
        system.scheduler().schedule(
                Duration.create(10000, TimeUnit.MILLISECONDS),
                Duration.create(1, TimeUnit.HOURS),
                getSelf(), new Grab(), system.dispatcher(), null);
    }

    //重写postRestart,防止preStart又一次进行初始化
    @Override
    public void postRestart(Throwable reason) throws Exception {
    }

    //重写preRestart，防止mongoActor受到影响
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        Grab grab = new Grab();
        //访问外网失败，重新访问
        getSelf().tell(grab, ActorRef.noSender());
        postStop();
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Grab) {
            try {
//                String pm25Json = PM25Grab.getStationData("福州");
//                List<PM25Msg> pm25Msgs = gson.fromJson(pm25Json, new TypeToken<List<PM25Msg>>() {
//                }.getType());
//                for (PM25Msg pm25Msg : pm25Msgs) {
//                    MongoPM25Station pm25Station = new MongoPM25Station();
//                    pm25Station.setCity(pm25Msg.getArea());
//                    //时间需要处理
//                    pm25Station.setLast_update(pm25Msg.getTime_point());
//                    pm25Station.setPm25_24h(pm25Msg.getPm2_5_24h());
//                    pm25Station.setPm25(pm25Msg.getPm2_5());
//                    pm25Station.setStation(pm25Msg.getPosition_name());
//                }
                /**
                 * TODO
                 * 获取全部城市，采集点的数据
                 */
                String json = PM25Grab.getCityData("");
                //格式化数据
                List<PM25Msg> pm25Msgs = gson.fromJson(json, new TypeToken<List<PM25Msg>>() {
                }.getType());
                //过滤字段，封装成MongoPM25City
                for (PM25Msg pm25Msg : pm25Msgs) {
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

        }
    }
}