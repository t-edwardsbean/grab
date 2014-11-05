package com.baidu.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by edwardsbean on 14-11-5.
 */
@Document(collection = "thinkPageCity")
public class MongoThinkPageCity {
    private String city;
    private String last_update;
    private String pm25;
    private String pm10;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }
}
