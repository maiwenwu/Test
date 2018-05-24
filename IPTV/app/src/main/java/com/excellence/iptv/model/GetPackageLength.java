package com.excellence.iptv.model;

import android.util.SparseArray;
import com.excellence.iptv.bean.DataPackage;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author maiwenwu
 * @date 2018/3/20
 */

public class GetPackageLength {

    private static final int PACKAGE_HAND = 0x47;
    private static final int TIME = 10;
    private static final int LENGTH_188 = 188;
    private static final int LENGTH_204 = 204;
    private static DataPackage dataPackage = new DataPackage();

    public static DataPackage getPackage(String filePath) {

        try {
            // I/O read file
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            boolean is188PackageLength = false;
            boolean is204PackageLength = false;
            Integer getByte;

            int count = 0;

            SparseArray<int[]> sparseArray188 = new SparseArray<>();
            SparseArray<int[]> sparseArray204 = new SparseArray<>();

            //read one bite
            while ((getByte = fis.read()) != -1) {

                if (getByte == PACKAGE_HAND) {

                    int result;
                    result = getPackageLength(LENGTH_188, count, sparseArray188);
                    if (result == -1) {
                        break;
                    }
                    result = getPackageLength(LENGTH_204, count, sparseArray204);
                    if (result == -1) {
                        break;
                    }
                }
                count++;
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataPackage;
    }

    public static int getPackageLength(int matchPackageLength, int count, SparseArray<int[]> sparseArray) {

        int intervalValue = count / matchPackageLength;
        int relativeValue = count % matchPackageLength;

        int[] data = sparseArray.get(relativeValue, null);

        if (data != null) {

            int startPosition = data[0];
            int value = data[1];
            int counter = data[2];

            if (counter == TIME) {
                dataPackage.setPackageLength(matchPackageLength);
                dataPackage.setPosition(startPosition);
                return -1;
            }

            if (intervalValue - value == 1) {
                data[1] = intervalValue;
                data[2] += 1;
                sparseArray.put(relativeValue, data);
            } else {
                sparseArray.delete(relativeValue);
            }

        } else {
            data = new int[3];
            data[0] = count;
            data[1] = intervalValue;
            data[2] = 1;
            sparseArray.put(relativeValue, data);
        }
        return 0;
    }
}
