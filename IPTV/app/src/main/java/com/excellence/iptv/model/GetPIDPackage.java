package com.excellence.iptv.model;

import android.os.Message;

import com.excellence.iptv.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author maiwenwu
 * @date 2018/3/20
 */

public class GetPIDPackage {

    private static final String PATH_END = "/table/";

    public static String getPIDPackage(int packageLength, int position, int pid, String fileName, String sdPath, String tsName) {

        String wFileName = null;

        if (packageLength == MainActivity.LENGTH_188 || packageLength == MainActivity.LENGTH_204) {

            try {

                File file = new File(fileName);
                FileInputStream fis = new FileInputStream(file);

                wFileName = sdPath + PATH_END + tsName;

                File fileFolder = new File(sdPath + PATH_END);
                if (!fileFolder.exists()){
                    fileFolder.mkdir();
                }

                File wFile = new File(wFileName);

                if (wFile.exists()) {
                    if (wFile.isFile()) {
                        wFile.delete();
                    }
                }
                wFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(wFile, true);

                byte[] buf = new byte[packageLength];
                fis.skip(position);

                int temp;

                while ((temp = fis.read(buf)) != -1) {

                    int packagePID = ((buf[1] & 0x001f) << 8) | (buf[2] & 0x00ff);

                    if ((packagePID & 0x1fff) == pid) {
                        fos.write(buf);
                    }

                    if (temp < packageLength) {
                        break;
                    }
                }

                fos.flush();
                fos.close();
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wFileName;
    }

}

