package com.baidu.model;

/**
 * 生活建议指数 **V1.0新增**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageSuggestion {
    public ThinkPageDressing getDressing() {
        return dressing;
    }

    public void setDressing(ThinkPageDressing dressing) {
        this.dressing = dressing;
    }

    public ThinkPageUV getUv() {
        return uv;
    }

    public void setUv(ThinkPageUV uv) {
        this.uv = uv;
    }

    public ThinkPageCarWashing getCar_washing() {
        return car_washing;
    }

    public void setCar_washing(ThinkPageCarWashing car_washing) {
        this.car_washing = car_washing;
    }

    public ThinkPageTravel getTravel() {
        return travel;
    }

    public void setTravel(ThinkPageTravel travel) {
        this.travel = travel;
    }

    public ThinkPageFlu getFlu() {
        return flu;
    }

    public void setFlu(ThinkPageFlu flu) {
        this.flu = flu;
    }

    public ThinkPageSport getSport() {
        return sport;
    }

    public void setSport(ThinkPageSport sport) {
        this.sport = sport;
    }

    private ThinkPageDressing dressing;
    private ThinkPageUV uv;
    private ThinkPageCarWashing car_washing;
    private ThinkPageTravel travel;
    private ThinkPageFlu flu;
    private ThinkPageSport sport;
}
