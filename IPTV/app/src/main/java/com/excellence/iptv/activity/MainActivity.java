package com.excellence.iptv.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.excellence.iptv.R;
import com.excellence.iptv.bean.DataPackage;
import com.excellence.iptv.bean.EitData;
import com.excellence.iptv.bean.Program;
import com.excellence.iptv.bean.SdtData;
import com.excellence.iptv.model.GetPackageLength;
import com.excellence.iptv.model.SolutionSdt;
import com.excellence.iptv.model.SolutionsEit;
import com.excellence.iptv.model.SolutionsPat;
import com.excellence.iptv.model.WaitingDialog;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maiwenwu
 * @date 2018/04/03
 */
public class MainActivity extends AppCompatActivity {

    private static final int SDT_PID = 0x0011;
    private static final int SDT_TABLE_ID = 0x42;
    private static final int PAT_PID = 0x0000;
    private static final int PAT_TABLE_ID = 0x00;
    private static final int PMT_TABLE_ID = 0x02;
    private static final int EIT_PID = 0x0012;
    private static final int EIT_TABLE_ID = 0x4e;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private long exitTime = 0;
    public static final int LENGTH_188 = 188;
    public static final int LENGTH_204 = 204;
    private static final int IS_PACKAGE_LENGTH = 1;
    private static final int SDT_MSG = 2;
    private static final int EIT_MSG = 3;
    private static final String SDT_SECTION = "sdt_section";
    private static final String SDT_TS_NAME = "sdt_ts";
    private static final String PAT_SECTION = "pat_section";
    private static final String PAT_TS_NAME = "pat_ts";
    private static final String EIT_TS_NAME = "eit_ts";
    private static final int TIME = 2000;

    private static List<File> mFileList;
    public static int packageLength;
    public static int position;

    private static String mSdPath;
    private ListView mLvFileList;
    private Dialog mDialog;
    private List<Map<String, String>> mList = new ArrayList<>();
    private String mFilePath;
    private List<EitData> mEitDataList = new ArrayList<>();
    private List<Program> mSdtDataList = new ArrayList<>();

    private MyHandler mMyHandler = new MyHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        TextView textView = findViewById(R.id.tv_title);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        textView.setTypeface(typeface);

        mLvFileList = findViewById(R.id.lv_file_list);

        mDialog = WaitingDialog.createLoadingDialog(this);
        mDialog.show();

        requestPermissions();

    }

    /**
     * Check and request permissions.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        getTsFile();
    }

    /**
     * pop up request authorization dialog box.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Permission Denied
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getTsFile();
                } else {
                    Toast.makeText(this, getString(R.string.access_denied), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getTsFile() {

        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {

            try {
                File sdFile = Environment.getExternalStorageDirectory();
                mSdPath = sdFile.getCanonicalPath();

                String sdPath = mSdPath + "/ts";

                File mSdFile = new File(sdPath);

                mFileList = new ArrayList<>();

                mFileList = filePath(mSdFile);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mDialog.dismiss();
            }
        }

        for (int i = 0; i < mFileList.size(); i++) {
            Map<String, String> map = new HashMap<>(mFileList.size());
            map.put("file_name", mFileList.get(i).getName());
            mList.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                this.mList,
                R.layout.file_list_activity,
                new String[]{"file_name"},
                new int[]{R.id.tv_file_name});

        mLvFileList.setAdapter(simpleAdapter);

        mLvFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mDialog = WaitingDialog.createLoadingDialog(MainActivity.this);
                mDialog.show();

                mFilePath = mFileList.get(i).getPath();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataPackage dataPackage = GetPackageLength.getPackage(mFilePath);

                        Message msg = new Message();
                        msg.obj = dataPackage;
                        msg.what = IS_PACKAGE_LENGTH;

                        mMyHandler.sendMessage(msg);

                    }
                }).start();
            }
        });
    }

    public List<File> filePath(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.listFiles() == null) {
                    mFileList.add(file2);
                } else {
                    filePath(file2);
                }
            }
        } else {
            System.out.println(getString(R.string.file_not_exit));
        }
        return mFileList;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > TIME) {
            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == IS_PACKAGE_LENGTH) {

                DataPackage dataPackage = (DataPackage) msg.obj;
                packageLength = dataPackage.getPackageLength();
                position = dataPackage.getPosition() ;

                if (packageLength == LENGTH_188 || packageLength == LENGTH_204) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<Integer> pmtPid = SolutionsPat.solutionsPat(mFilePath, mSdPath, PAT_SECTION, PAT_TS_NAME, PAT_PID, PAT_TABLE_ID, packageLength, position);
                            SolutionSdtThread solutionSdtThread = new SolutionSdtThread(mFilePath, mSdPath, SDT_SECTION, SDT_TS_NAME, SDT_PID, SDT_TABLE_ID, packageLength, position);
                            Thread sdtThread = new Thread(solutionSdtThread);
                            sdtThread.start();

                            SolutionEitThread solutionEitThread = new SolutionEitThread(mFilePath,mSdPath,EIT_TS_NAME,EIT_PID,EIT_TABLE_ID,packageLength,position);
                            Thread eitThread = new Thread(solutionEitThread);
                            eitThread.start();

                        }
                    }).start();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_package_length), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }

            if (msg.what == EIT_MSG) {

                Gson gson = new Gson();

                Intent intent = new Intent(MainActivity.this, FragmentMainActivity.class);
//                intent.setAction("my_action");
//                intent.addCategory("my_category");
                intent.putExtra("sdtDataList", gson.toJson(mSdtDataList));
                intent.putExtra("eitDataList", gson.toJson(mEitDataList));
                startActivity(intent);
                finish();
            }

        }
    }

    public class SolutionSdtThread implements Runnable {

        private String mFileName;
        private String mSdPath;
        private String mSdtSection;
        private String mSdtTsName;
        private int mPid;
        private int mTableId;
        private int mPackageLength;
        private int mPosition;

        SolutionSdtThread(String fileName, String sdPath, String sdtSection, String sdtTsName, int pid, int tableId, int packageLength, int position) {
            this.mFileName = fileName;
            this.mSdPath = sdPath;
            this.mSdtSection = sdtSection;
            this.mSdtTsName = sdtTsName;
            this.mPid = pid;
            this.mTableId = tableId;
            this.mPackageLength = packageLength;
            this.mPosition = position;
        }

        @Override
        public void run() {
            try {
                mSdtDataList = SolutionSdt.solutionSdt(mFileName, mSdPath, mSdtSection, mSdtTsName, mPid, mTableId, mPackageLength, mPosition);
                Message msg = new Message();
                mMyHandler.sendEmptyMessage(SDT_MSG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SolutionEitThread implements Runnable {

        private String mFileName;
        private String mSdPath;
        private String mEitTsName;
        private int mPid;
        private int mTableId;
        private int mPackageLength;
        private int mPosition;

        public SolutionEitThread(String fileName, String sdPath, String eitTsName, int pid, int tableId, int packageLength, int position) {
            this.mFileName = fileName;
            this.mSdPath = sdPath;
            this.mEitTsName = eitTsName;
            this.mPid = pid;
            this.mTableId = tableId;
            this.mPackageLength = packageLength;
            this.mPosition = position;
        }

        @Override
        public void run() {
            try {
                mEitDataList = SolutionsEit.solutionsEit(mFileName,mSdPath,mEitTsName,mPid,mTableId,mPackageLength,mPosition);
                Message msg = new Message();
                mMyHandler.sendEmptyMessage(EIT_MSG);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mDialog.dismiss();
            }
        }
    }
}
