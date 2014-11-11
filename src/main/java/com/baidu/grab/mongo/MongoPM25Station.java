package com.baidu.grab.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by edwardsbean on 14-11-5.
 */
@Document(collection = "pm25Station")
public class MongoPM25Station {
    private String city;
    private String last_update;
    private String pm25;
    private String pm25_24h;
    private String station;

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

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

    public String getPm25_24h() {
        return pm25_24h;
    }

    public void setPm25_24h(String pm25_24h) {
        this.pm25_24h = pm25_24h;
    }
}
