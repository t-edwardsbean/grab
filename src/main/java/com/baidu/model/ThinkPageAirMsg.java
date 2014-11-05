package com.baidu.model;


import java.util.List;

/**
 * 获取指定城市的AQI、PM2.5、PM10、一氧化碳、二氧化氮、臭氧等空气质量信息。
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageAirMsg {
    private String status;
    private List<ThinkPageWeather> weather;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ThinkPageWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<ThinkPageWeather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ThinkPageAirMsg{" +
                "status='" + status + '\'' +
                ", weather=" + weather +
                '}';
    }
}
