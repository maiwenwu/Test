package com.excellence.iptv.bean;

import java.util.Arrays;

/**
 * @author maiwenwu
 * @date 2018/4/24
 */

public class EitSection {

    private int sectionLength;
    private int sectionNumber;
    private int lastSectionNumber;
    private int serviceId;
    private byte[] sectionData;
    private int count;
    private int versionNumber;

    public EitSection() {
    }

    public EitSection(int sectionLength, int sectionNumber, int lastSectionNumber, int serviceId, byte[] sectionData, int count, int versionNumber) {
        this.sectionLength = sectionLength;
        this.sectionNumber = sectionNumber;
        this.lastSectionNumber = lastSectionNumber;
        this.serviceId = serviceId;
        this.sectionData = sectionData;
        this.count = count;
        this.versionNumber = versionNumber;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public byte[] getSectionData() {
        return sectionData;
    }

    public void setSectionData(byte[] sectionData) {
        this.sectionData = sectionData;
    }

    public int getSectionLength() {
        return sectionLength;
    }

    public void setSectionLength(int sectionLength) {
        this.sectionLength = sectionLength;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getLastSectionNumber() {
        return lastSectionNumber;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public void setLastSectionNumber(int lastSectionNumber) {
        this.lastSectionNumber = lastSectionNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Section [sectionLength=" + sectionLength + ", sectionNumber="
                + sectionNumber + ", lastSectionNumber=" + lastSectionNumber
                + ", serviceId=" + serviceId + ", sectionData="
                + Arrays.toString(sectionData) + ", count=" + count
                + ", versionNumber=" + versionNumber + "]";
    }

}
