package com.baidu.grab;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.baidu.GrabMain;
import com.baidu.mongo.MongoPM25City;
import org.springframework.data.mongodb.core.MongoTemplate;
import scala.Option;


/**
 * 写数据到Mongo
 * Created by edwardsbean on 14-11-5.
 */
public class MongoActor extends UntypedActor {
    MongoTemplate mongoTemplate;
    MongoPM25City currentProcess;

    public MongoActor() {
        mongoTemplate = (MongoTemplate) GrabMain.applicationContext.getBean("mongoTemplate");
    }


    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        //数据恢复，重新发送回邮箱
        getSelf().tell(currentProcess, ActorRef.noSender());
        currentProcess = null;
        postStop();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        /**
         *  如果直接传List<MongoPM25City>，那么一条记录保存失败，导致这次的数据全部失败。
         *  在进行数据恢复时，会导致重复数据。所以这里每次只收一个MongoPM25City
         */
        if (message instanceof MongoPM25City) {
            MongoPM25City city = (MongoPM25City) message;
            currentProcess = city;
            mongoTemplate.save(city);
        }
    }
}
