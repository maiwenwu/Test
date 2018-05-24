package com.excellence.iptv.model;

import com.excellence.iptv.bean.PmtData;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/2
 */

public class SolutionsPmt {

    private static final int CONDITION = 2;
    private static final int TIME = 5;

    public static void solutionsPmt(String fileName, String sdPath,String section, String tsName, int pid, int tableId, int packageLength, int position) {

        int programInfoLength;
        int streamType;
        int elementaryPid;
        int eSInfoLength;
        int descriptor;

        List<PmtData> pmtDataList = new ArrayList<>();

        String sectionName = GetSection.getSection(fileName, sdPath,section,tsName, pid, tableId, packageLength, position);

        int sectionLength = GetSection.sectionLength;

        try {

            File file = new File(sectionName);

            FileInputStream fis = new FileInputStream(file);

            byte[] buf = new byte[sectionLength + 3];

            while (fis.read(buf) != -1) {

                programInfoLength = ((buf[10] & 0xff) & 0x0F) << 8 | (buf[11] & 0xff);

                int pos = 12;

                if (programInfoLength != 0) {
                    pos = pos + programInfoLength;
                }

                for (; pos <= (sectionLength - CONDITION); ) {

                    PmtData pmtData = new PmtData();

                    streamType = buf[pos] & 0xff;
                    elementaryPid = (((buf[pos + 1] & 0xff) << 8) | (buf[pos + 2] & 0xff)) & 0x1FFF;
                    eSInfoLength = ((buf[pos + 3] & 0x0F) << 8 | buf[pos + 4] & 0xff);

                    pmtData.setStreamType(streamType);
                    pmtData.setElementaryPid(elementaryPid);

                    pmtDataList.add(pmtData);

                    if (eSInfoLength != 0) {
                        descriptor = buf[pos + 5];
                        for (int len = CONDITION; len <= eSInfoLength; len++) {
                            descriptor = descriptor << 8 | buf[pos + 4 + len];
                        }
                        pos = pos + eSInfoLength;
                    }
                    pos = pos + TIME;
                }
            }

            fis.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
