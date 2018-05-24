package com.excellence.iptv.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.excellence.iptv.R;
import com.excellence.iptv.bean.Program;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/04/18
 */
public class PlayActivity extends AppCompatActivity {

    private ImageView mIvPlay;
    private TextView mTvProgramNum;
    private TextView mTvProgramName;
    private TextView mTvProgramTime;
    private TextView mTvProgramEpg;
    private ImageView mIvBackground;
    private Boolean mIsPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_activity);

        mIvPlay = findViewById(R.id.iv_play);
        mIvBackground = findViewById(R.id.iv_background);
        mTvProgramNum = findViewById(R.id.tv_program_num);
        mTvProgramName = findViewById(R.id.tv_program_name);
        mTvProgramTime = findViewById(R.id.tv_program_time);
        mTvProgramEpg = findViewById(R.id.tv_program_epg);

        String programStr = getIntent().getStringExtra("program");

        Program program = new Gson().fromJson(programStr, new TypeToken<Program>() {
        }.getType());

        mIvBackground.setBackgroundResource(Integer.parseInt(program.getFavoritePic()));
        mTvProgramNum.setText(program.getProgramNum()+"");
        mTvProgramName.setText(program.getProgramName());
        mTvProgramTime.setText(program.getProgramTime());
        mTvProgramEpg.setText(program.getEpg());

        mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsPlay){
                    mIvPlay.setImageResource(R.drawable.ic_play);
                    mIsPlay = false;
                }else {
                    mIvPlay.setImageResource(R.drawable.ic_pause);
                    mIsPlay = true;
                }
            }
        });
    }
}
