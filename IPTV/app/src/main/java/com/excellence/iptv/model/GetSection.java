package com.excellence.iptv.model;

import com.excellence.iptv.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author maiwenwu
 * @date 2018/3/21
 */

public class GetSection {

    private static final int PAYLOAD_1_INVALID_BIT = 8;
    private static final int PAYLOAD_1_BEGIN_BIT = 5;
    private static final int PAYLOAD_0_INVALID_BIT = 7;
    private static final int PAYLOAD_0_BEGIN_BIT = 4;
    private static final int INTO_AN_INTEGER = 0xff;
    private static final int CHECK_CODE = 16;

    public static int sectionLength;

    private static int mTableId;
    private static int sectionLengths = 0;
    private static String mSectionName;

    public static String getSection(String fileName, String sdPath, String section, String tsName, int pid, int tableId, int packageLength, int position) {

        sectionLength = 0;

        String wFileName = GetPIDPackage.getPIDPackage(packageLength, position, pid, fileName, sdPath, tsName);

        byte[] buf = new byte[packageLength];

        try {

            File file = new File(wFileName);
            FileInputStream fis = new FileInputStream(file);

            mSectionName = sdPath + "/table/" + section;

            File wFile = new File(mSectionName);

            if (wFile.exists()) {
                if (wFile.isFile()) {
                    wFile.delete();
                }
            }
            wFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(wFile, true);

            while (fis.read(buf) != -1) {

                if ((buf[1] & 0x40) == 0x40) {

                    if (packageLength == MainActivity.LENGTH_204) {
                        packageLength = packageLength - CHECK_CODE;
                    }

                    mTableId = buf[5] & INTO_AN_INTEGER;
                    sectionLength = (((buf[6] & INTO_AN_INTEGER) << 8) | (buf[7] & INTO_AN_INTEGER)) & 0x0fff;

                    sectionLengths = sectionLength;

                    if (mTableId == tableId) {
                        if (sectionLengths < packageLength - PAYLOAD_1_BEGIN_BIT) {
                            for (int i = PAYLOAD_1_BEGIN_BIT; i < sectionLengths + PAYLOAD_1_INVALID_BIT; i++) {
                                fos.write(buf[i]);
                            }
                            break;
                        } else {
                            for (int i = PAYLOAD_1_BEGIN_BIT; i < packageLength; i++) {
                                fos.write(buf[i]);
                            }
                            sectionLengths = sectionLengths - packageLength + PAYLOAD_1_BEGIN_BIT;
                        }
                    }

                } else {
                    if (sectionLength == 0) {
                        continue;
                    }
                    if (sectionLengths < packageLength - PAYLOAD_0_BEGIN_BIT) {
                        for (int i = PAYLOAD_0_BEGIN_BIT; i < sectionLengths + PAYLOAD_0_INVALID_BIT; i++) {
                            fos.write(buf[i]);
                        }
                        break;
                    } else {
                        for (int i = PAYLOAD_0_BEGIN_BIT; i < packageLength; i++) {
                            fos.write(buf[i]);
                        }
                        sectionLengths = sectionLengths - packageLength + PAYLOAD_0_BEGIN_BIT;
                    }
                }
            }

            fos.flush();
            fos.close();
            fis.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mSectionName;
    }

}
