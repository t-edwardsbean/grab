package com.baidu.model;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageWeather {
    private String city_name;
    private String city_id;
    private String last_update;
    private ThinkPageAirQuality air_quality;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public ThinkPageAirQuality getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(ThinkPageAirQuality air_quality) {
        this.air_quality = air_quality;
    }

    @Override
    public String toString() {
        return "ThinkPageWeather{" +
                "city_name='" + city_name + '\'' +
                ", city_id='" + city_id + '\'' +
                ", last_update='" + last_update + '\'' +
                ", air_quality=" + air_quality +
                '}';
    }
}
