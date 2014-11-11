package com.baidu.grab.message;

/**
 * Created by edwardsbean on 14-11-6.
 */
public class Grab {
    private String city;

    public Grab(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Grab{" +
                "city='" + city + '\'' +
                '}';
    }
}
