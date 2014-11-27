package com.baidu.grab.core;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.baidu.grab.GrabMain;
import com.baidu.grab.mongo.MongoPM25City;
import com.baidu.grab.mongo.MongoPM25Station;
import org.springframework.data.mongodb.core.MongoTemplate;
import scala.Function0;
import scala.Option;
import scala.runtime.AbstractFunction0;


/**
 * 写数据到Mongo
 * Created by edwardsbean on 14-11-5.
 */
public class MongoActor extends UntypedActor {
    MongoTemplate mongoTemplate;

    public MongoActor() {
        mongoTemplate = (MongoTemplate) GrabMain.applicationContext.getBean("mongoTemplate");
    }


    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        //数据恢复，重新发送回邮箱
        getSelf().tell(message.getOrElse(new AbstractFunction0<Object>(){
            @Override
            public Object apply() {
                return "空消息";
            }
        }), ActorRef.noSender());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        /**
         *  如果直接传List<MongoPM25City>，那么一条记录保存失败，导致这次的数据全部失败。
         *  在进行数据恢复时，会导致重复数据。所以这里每次只收一个MongoPM25City
         */
        if (message instanceof MongoPM25City) {
            MongoPM25City city = (MongoPM25City) message;
            mongoTemplate.save(city);
        } else if (message instanceof MongoPM25Station) {
            MongoPM25Station station = (MongoPM25Station) message;
            mongoTemplate.save(station);
        } else {
            unhandled(message);
        }
    }
}
