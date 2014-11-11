package com.baidu.grab.schedule;


import java.util.List;

/**
 * 城市字典
 * 1：pm25.in一次性只能获取全部110城市空气质量，额外的80个城市
 * 2：190个城市
 * Created by edwardsbean on 14-11-11.
 */
public class CityDict {
    private List<String> allCity;

    public List<String> getAllCity() {
        return allCity;
    }

    public void setAllCity(List<String> allCity) {
        this.allCity = allCity;
    }
}
