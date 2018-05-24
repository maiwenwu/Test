package com.excellence.iptv.bean;

/**
 * @author maiwenwu
 * @date 2018/4/2
 */

public class PmtData {

    private int streamType;
    private int elementaryPid;

    public int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getElementaryPid() {
        return elementaryPid;
    }

    public void setElementaryPid(int elementaryPid) {
        this.elementaryPid = elementaryPid;
    }

}
