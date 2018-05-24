package com.excellence.iptv.model;

import com.excellence.iptv.bean.EitSection;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/24
 */

public class GetEitSection {

    private static int mTableId;
    private static final int PAYLOAD_1_INVALID_BIT = 5;
    private static final int PAYLOAD_0_INVALID_BIT = 4;
    private static final int TABLE_HEAD_BIT = 3;

    public static List<EitSection> getsList(String fileName, String sdPath, String tsName, int pid, int tableId, int packageLength, int position) {

        String wFileName = GetPIDPackage.getPIDPackage(packageLength, position, pid, fileName, sdPath, tsName);

        byte[] buf = new byte[packageLength];

        byte[] sectionData;

        int versionNumber;
        int sectionLength = 0;
        int sectionNumber;
        int serviceId;
        EitSection eitSection = null;
        List<EitSection> sections = new ArrayList<>();

        try {

            File file = new File(wFileName);
            FileInputStream fis = new FileInputStream(file);

            while (fis.read(buf) != -1) {

                if ((buf[1] & 0x40) == 0x40) {

                    if (packageLength == 204) {
                        packageLength = packageLength - 16;
                    }

                    sectionNumber = buf[11] & 0xff;
                    serviceId = ((buf[8] & 0xff) << 8) | (buf[9] & 0xff);
                    versionNumber = ((buf[10] & 0xff) & 0x3e) >> 1;

                    if (!sections.isEmpty()) {
                        boolean isOld = false;
                        for (int i = 0; i < sections.size(); i++) {
                            if (serviceId == sections.get(i).getServiceId() && sectionNumber == sections.get(i).getSectionNumber()) {
                                isOld = true;
                                break;
                            }
                        }
                        if (isOld) {
                            continue;
                        }
                    }
                    mTableId = buf[5] & 0xff;
                    sectionLength = (((buf[6] & 0xff) << 8) | (buf[7] & 0xff)) & 0x0fff;

                    if (mTableId != tableId) {
                        sectionLength = 0;
                        continue;
                    }

                    sectionData = new byte[sectionLength + 3];
                    int counter = 0;

                    if (sectionLength < packageLength - PAYLOAD_1_INVALID_BIT) {
                        for (int i = 0; i < sectionLength + TABLE_HEAD_BIT; i++) {
                            sectionData[i] = (byte) (buf[i + 5] & 0xff);
                            counter++;
                        }
                    } else {
                        for (int i = 0; i < packageLength - PAYLOAD_1_INVALID_BIT; i++) {
                            sectionData[i] = (byte) (buf[i + 5] & 0xff);
                            counter++;
                        }
                    }

                    eitSection = new EitSection();
                    eitSection.setCount(counter);
                    eitSection.setSectionNumber(sectionNumber);
                    eitSection.setServiceId(serviceId);
                    eitSection.setVersionNumber(versionNumber);
                    eitSection.setSectionData(sectionData);

                    if ((sectionLength + 3) == counter) {
                        sections.add(eitSection);
                    }

                } else {

                    if (sectionLength == 0) {
                        continue;
                    }

                    int counter1 = eitSection.getCount();
                    byte[] sectionData1 = eitSection.getSectionData();
                    int nextCount = (sectionLength + 3 - counter1);
                    if (nextCount < (packageLength - 4)) {
                        for (int i = 0; i < nextCount; i++) {
                            sectionData1[counter1] = (byte) (buf[i + 4] & 0xff);
                            counter1++;
                        }
                    } else {
                        for (int i = 0; i < packageLength - PAYLOAD_0_INVALID_BIT; i++) {
                            sectionData1[counter1] = (byte) (buf[i + 4] & 0xff);
                            counter1++;
                        }
                    }
                    eitSection.setCount(counter1);
                    eitSection.setSectionData(sectionData1);

                    if ((sectionLength + 3) == counter1) {
                        sections.add(eitSection);
                    }
                }
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sections;
    }
}
