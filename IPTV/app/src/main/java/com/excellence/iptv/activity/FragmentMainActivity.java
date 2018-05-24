package com.excellence.iptv.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.excellence.iptv.R;
import com.excellence.iptv.bean.EitData;
import com.excellence.iptv.bean.Program;
import com.excellence.iptv.flowLayout.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author maiwenwu
 * @date 2018/04/08
 */
public class FragmentMainActivity extends FragmentActivity implements View.OnClickListener {

    private LiveFragment mLiveFragment;
    private AboutFragment mAboutFragment;
    private SearchFragment mSearchFragment;
    private FavoriteFragment mFavoriteFragment;

    private View mLlBottom = null;

    private ImageView mIvLiveImage = null;
    private TextView mTvLiveText = null;
    private ImageView mIvAboutImage = null;
    private TextView mTvAboutText = null;

    private FragmentManager mFragmentManager = null;

    public  List<Program> programList = new ArrayList<>();
    private FlowLayout mFlowLayout;

    public List<Program> favorites = new ArrayList<>();

    private SharedPreferences mSharedPreferences;

    private int[] pic = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_main_activity);

        initView();

        String sdtDataListJson = getIntent().getStringExtra("sdtDataList");
        String eitDataListJson = getIntent().getStringExtra("eitDataList");

        programList = new Gson().fromJson(sdtDataListJson,new TypeToken<List<Program>>(){}.getType());
        List<EitData> eitDataList = new Gson().fromJson(eitDataListJson, new TypeToken<List<EitData>>() {
        }.getType());

        for (int i = 0 ; i < programList.size() ; i ++) {
            for (int j = 0; j < eitDataList.size() ; j ++) {
                if (programList.get(i).getProgramNum() == eitDataList.get(j).getServiceId()){
                    programList.get(i).setProgramTime(eitDataList.get(j).getTime());
                    programList.get(i).setEpg(eitDataList.get(j).getEventName());
                }
            }
            if(programList.get(i).getEpg() == null) {
                programList.get(i).setEpg("NO EPG");
            }
            programList.get(i).setFavoritePic(String.valueOf(pic[new Random().nextInt(9)]));
        }

        mFragmentManager = getFragmentManager();

        favorites = getFavorite();

        for (int i = 0 ; i < favorites.size() ; i ++) {
            if (favorites.get(i).isFavorite()){
                programList.get(i).setFavorite(true);
            }
        }

        selectionFragment(0);
    }

    /**
    * @description: initialize controls and setOnClickListener event
    */
    public void initView() {

        mFragmentManager = getFragmentManager();
        View liveLayout = findViewById(R.id.live_layout);
        View aboutLayout = findViewById(R.id.about_layout);

        mLlBottom = findViewById(R.id.ll_bottom);

        mIvLiveImage = findViewById(R.id.iv_live_image);
        mTvLiveText = findViewById(R.id.tv_live_text);

        mIvAboutImage = findViewById(R.id.iv_about_image);
        mTvAboutText = findViewById(R.id.tv_about_text);

        liveLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.live_layout:
                selectionFragment(0);
                break;

            case R.id.about_layout:
                selectionFragment(1);
                break;

            case R.id.ll_search :
                selectionFragment(2);
                break;

            case R.id.iv_favorite :
                selectionFragment(3);
                break;

            default:
                break;
        }

    }

    /**
    * @description: select fragment by index
    */
    public void selectionFragment(int index) {

        clearSelection();

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        switch (index) {
            case 0:
                mLlBottom.setVisibility(View.VISIBLE);
                mIvLiveImage.setImageResource(R.drawable.bottom_icon_live_light);
                mTvLiveText.setTextColor(Color.parseColor("#fefefe"));

                if (mLiveFragment == null){
                    mLiveFragment = new LiveFragment();
                }
                transaction.replace(R.id.content,mLiveFragment);
                break;

            case 1:
                mIvAboutImage.setImageResource(R.drawable.bottom_icon_about_light);
                mTvAboutText.setTextColor(Color.parseColor("#fefefe"));

                if (mAboutFragment == null){
                    mAboutFragment = new AboutFragment();
                }
                transaction.replace(R.id.content,mAboutFragment);
                break;

            case 2:
                mLlBottom.setVisibility(View.GONE);

                if (mSearchFragment == null){
                    mSearchFragment = new SearchFragment();
                }
                transaction.replace(R.id.content,mSearchFragment);
                break;

            case 3:

                mLlBottom.setVisibility(View.GONE);

                if (mFavoriteFragment == null){
                    mFavoriteFragment = new FavoriteFragment();
                }
                transaction.replace(R.id.content,mFavoriteFragment);
                break;

            default:
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
    * @description: clear the state of the bottom navigation bar.
    */
    public void clearSelection() {

        mIvLiveImage.setImageResource(R.drawable.bottom_icon_live_normal);
        mTvLiveText.setTextColor(Color.parseColor("#4dffffff"));

        mIvAboutImage.setImageResource(R.drawable.bottom_icon_about_normal);
        mTvAboutText.setTextColor(Color.parseColor("#4dffffff"));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 1) {
                while (mFragmentManager.getBackStackEntryCount() > 1) {
                    mFragmentManager.popBackStackImmediate();
                    mLlBottom.setVisibility(View.VISIBLE);
                    mIvLiveImage.setImageResource(R.drawable.bottom_icon_live_light);
                    mTvLiveText.setTextColor(Color.parseColor("#fefefe"));
                    mIvAboutImage.setImageResource(R.drawable.bottom_icon_about_normal);
                    mTvAboutText.setTextColor(Color.parseColor("#4dffffff"));
                }
            }else {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    /**
    * @description: save favorite program
    */
    public void saveFavorite() {

//        favorites.clear();

        Gson gson = new Gson();
        String favorite = gson.toJson(programList);

        mSharedPreferences = getSharedPreferences(String.valueOf(programList.get(0).getProgramNum()), MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("favorite", favorite);
        editor.commit();
    }

    /**
    * @description: get data from sharedPreferences
    */
    public List<Program> getFavorite() {

        List<Program> mProgramList = new ArrayList<>();
        mSharedPreferences = getSharedPreferences(String.valueOf(programList.get(0).getProgramNum()), MODE_PRIVATE);
        String json = mSharedPreferences.getString("favorite", null);

        if (json != null) {
            Gson gson = new Gson();
            mProgramList = gson.fromJson(json, new TypeToken<List<Program>>() {}.getType());
        }
        return mProgramList;
    }

    /**
    * @description: when fragment destroy,save data to sharedPreferences
    */
    @Override
    protected void onDestroy() {
        saveFavorite();
        super.onDestroy();
    }
}
