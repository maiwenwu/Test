package com.excellence.iptv.bean;

import java.io.Serializable;

/**
 * @author maiwenwu
 * @date 2018/4/24
 */

public class EitData implements Serializable{

    private String time;
    private int serviceId;
    private String eventName;

    public EitData(String time, String eventName, int serviceId) {
        super();
        this.time = time;
        this.eventName = eventName;
        this.serviceId = serviceId;
    }
    public EitData() {
        super();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

