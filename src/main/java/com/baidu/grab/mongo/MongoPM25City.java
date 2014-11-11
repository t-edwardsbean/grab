package com.baidu.grab.mongo;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class MongoPM25City {
    private String city;
    private String last_update;
    private String pm25;
    private String pm25_24h;

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
