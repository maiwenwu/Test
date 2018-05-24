package com.excellence.iptv.model;

import android.util.Log;

import com.excellence.iptv.bean.EitData;
import com.excellence.iptv.bean.EitSection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/24
 */

public class SolutionsEit {

    private static final int CONDITION = 18;
    private static final int TIME = 60;

    private static List<EitData> mEitDataList = new ArrayList<>();

    public static List<EitData> solutionsEit(String fileName, String sdPath, String eitTsName, int pid, int tableId, int packageLength, int position) throws Exception {

        List<EitSection> eitSections = GetEitSection.getsList(fileName, sdPath, eitTsName, pid, tableId, packageLength, position);

        for (int i = 0; i < eitSections.size(); i++) {

            EitData eitData = new EitData();

            int serviceId = eitSections.get(i).getServiceId();

            byte[] section = eitSections.get(i).getSectionData();
            int sectionLength = eitSections.get(i).getCount() - 3;

            for (int j = 0; j < sectionLength - CONDITION; ) {

                int runningStatus = (section[24 + j] & 0xff) >> 5;
                int descriptorsLoopLength = (((section[24 + j] & 0xff) << 8) | (section[25 + j] & 0xff)) & 0x0fff;

                int hour = Integer.parseInt(Integer.toHexString(section[18 + j] & 0xff));
                int minutes = Integer.parseInt(Integer.toHexString(section[19 + j] & 0xff));

                int durationHour = Integer.parseInt(Integer.toHexString(section[21 + j] & 0xff));
                int durationMinutes = Integer.parseInt(Integer.toHexString(section[22 + j] & 0xff));

                int time = hour * TIME + minutes + durationHour * TIME + durationMinutes;

                int endHour = time / TIME;
                int endMinutes = time % TIME;

                String startTime = String.valueOf(hour) + minutes;
                String endTime = String.valueOf(endHour) + endMinutes;
                SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat formatter2 = new SimpleDateFormat("HHmm");
                startTime = formatter1.format(formatter2.parse(startTime));
                endTime = formatter1.format(formatter2.parse(endTime));

                String durationTime = startTime + "-" + endTime;

                eitData.setTime(durationTime);

                for (int k = 0; k < descriptorsLoopLength; ) {
                    int descriptorTag = section[26 + j + k] & 0xff;
                    int descriptorLength = section[27 + j + k] & 0xff;

                    if (descriptorTag == 0x4d) {
                        int eventNameLength = section[31 + j + k] & 0xff;
                        int eventName;
                        byte[] eventNameBuf = new byte[eventNameLength];
                        byte[] realEventNameBuf = new byte[eventNameLength - 3];

                        for (int l = 0; l < eventNameBuf.length; l++) {
                            eventName = section[32 + j + l] & 0xff;
                            eventNameBuf[l] = (byte) eventName;
                        }

                        if (eventNameBuf[0] == 0x10) {
                            System.arraycopy(eventNameBuf, 3, realEventNameBuf, 0, realEventNameBuf.length);
                        }

                        String realEventName = new String(realEventNameBuf, "ISO8859-2");
                        eitData.setEventName(realEventName);

                    }
                    k = k + descriptorLength + 2;
                }
                eitData.setServiceId(serviceId);
                if (runningStatus == 0x4) {
                    mEitDataList.add(eitData);
                }
                j = j + descriptorsLoopLength + 12;
            }
        }

        return mEitDataList;
    }
}
