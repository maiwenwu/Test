package com.excellence.iptv.model;

import com.excellence.iptv.bean.Program;
import com.excellence.iptv.bean.SdtData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/3
 */

public class SolutionSdt {

    private static final int CONDITION = 15;
    private static final int TIME = 5;

    public static List<Program> solutionSdt(String fileName, String sdPath, String sdtSection,String sdtTsName, int pid, int tableId, int packageLength, int position) {

        String sectionName = GetSection.getSection(fileName, sdPath, sdtSection, sdtTsName, pid, tableId, packageLength, position);

        int sectionLength = GetSection.sectionLength;

        List<Program> sdtDataList = new ArrayList<>();

        int serviceId;
        int descriptorsLoopLength;
        int serviceProviderNameLength;
        String serviceNameStr;
        byte[] serviceNameBuf;
        int serviceNameLength;
        int serviceName;

        byte[] buf = new byte[sectionLength + 3];

        try {

            File file = new File(sectionName);

            FileInputStream fis = new FileInputStream(file);

            while (fis.read(buf) != -1) {

                for (int i = 0; i < sectionLength - CONDITION; ) {

                    Program program = new Program();

                    serviceId = ((buf[11 + i] & 0xff) << 8) | (buf[12 + i] & 0xff);
                    program.setProgramNum(serviceId);

                    descriptorsLoopLength = (((buf[14 + i] & 0xff) << 8) | (buf[15 + i] & 0xff)) & 0x0fff;
                    serviceProviderNameLength = buf[19 + i] & 0xff;
                    serviceNameLength = buf[20 + serviceProviderNameLength + i] & 0xff;

                    serviceNameBuf = new byte[serviceNameLength];

                    for (int j = 0; j < serviceNameLength; j++) {

                        serviceName = buf[21 + serviceProviderNameLength + i + j];
                        serviceNameBuf[j] = (byte) serviceName;

                    }

                    serviceNameStr = new String(serviceNameBuf);
                    program.setProgramName(serviceNameStr);

                    sdtDataList.add(program);

                    i = i + descriptorsLoopLength + TIME;
                }

            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sdtDataList;

    }

}
