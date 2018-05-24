package com.excellence.iptv.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/3/28
 */

public class SolutionsPat {

    private static final int INTO_AN_INTEGER = 0xff;
    private static final int HEAD_BIT = 3;
    private static final int TIME = 4;
    private static final int CONDITION = 12;

    public static List<Integer> solutionsPat(String fileName, String sdPath, String patSection,String patTsName,int pid, int tableId, int packageLength, int position){

        String sectionFileName = GetSection.getSection(fileName,sdPath,patSection,patTsName,pid, tableId, packageLength,position);

        int sectionLength = GetSection.sectionLength;

        List<Integer> pmtPid = new ArrayList<>();

        try{

            File file = new File(sectionFileName);

            FileInputStream fis = new FileInputStream(file);

            byte[] buf = new byte[sectionLength + HEAD_BIT];

            while (fis.read(buf) != -1) {

                for (int n = 0; n < sectionLength - CONDITION; n += TIME) {
                    int mProgramNumber = ((buf[8 + n] & INTO_AN_INTEGER) << 8) | (buf[9 + n] & INTO_AN_INTEGER);

                    if (mProgramNumber == 0x00) {
                        int mNetworkPid = ((buf[10 + n] & INTO_AN_INTEGER) & 0x1F) << 8 | (buf[11 + n] & INTO_AN_INTEGER);
                    }else{
                        int mProgramMapPid = ((buf[10 + n] & INTO_AN_INTEGER) & 0x1F) << 8 | (buf[11 + n] & INTO_AN_INTEGER);
                        pmtPid.add(mProgramMapPid);
                    }
                }
            }

            fis.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        return pmtPid;
    }

}
