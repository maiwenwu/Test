package com.excellence.iptv.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellence.iptv.R;
import com.excellence.iptv.baseadapter.SearchAdapter;
import com.excellence.iptv.bean.Program;
import com.excellence.iptv.flowLayout.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author PC-001
 * @date 2018/04/11
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final int CYCLE_TIME = 6;

    private FlowLayout mFlowLayout;
    private EditText mEtSearchContent;
    private List<String> mInfoList;
    private SharedPreferences mSharedPreferences;
    private FragmentMainActivity mFragmentMainActivity = null;
    private List<String> mSearchHistory = new ArrayList<>();
    private List<Program> mProgramList = new ArrayList<>();
    private List<Program> mSearchList;
    private Context mContext;
    private ListView mLvSearchResult;
    private SearchAdapter mSearchAdapter = null;
    private Boolean mIsLongClick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mFragmentMainActivity = (FragmentMainActivity) getActivity();

        mProgramList = mFragmentMainActivity.programList;

        ImageView ivDelete = view.findViewById(R.id.iv_delete);
        mFlowLayout = view.findViewById(R.id.flow_layout);
        ImageView ivSearch = view.findViewById(R.id.iv_search);
        mEtSearchContent = view.findViewById(R.id.et_search_content);
        mLvSearchResult = view.findViewById(R.id.lv_search);
        Button btnBack = view.findViewById(R.id.btn_back);

        mContext = getActivity();
        mSearchList = new ArrayList<>();

        mInfoList = new ArrayList<>();
        mInfoList = getInfo();

        showInfo();

        mEtSearchContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mEtSearchContent.setText("");
                }
            }
        });

        ivDelete.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        return view;
    }

    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_search:
                if ("".equals(mEtSearchContent.getText().toString())) {
                    break;
                } else {
                    saveInfo();
                    showInfo();
                    mSearchList.clear();
                    mLvSearchResult.setVisibility(View.VISIBLE);
                    String search = mEtSearchContent.getText().toString();
                    mSearchList = searchResult(search);
                    mSearchAdapter = new SearchAdapter(mSearchList, mContext);
                    mLvSearchResult.setAdapter(mSearchAdapter);

                    mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Gson gson = new Gson();
                            Intent intent = new Intent(getActivity(), PlayActivity.class);
                            intent.putExtra("program", gson.toJson(mSearchList.get(i)));
                            startActivity(intent);
                        }
                    });
                }
                mEtSearchContent.setText("");
                break;

            case R.id.iv_delete:
                mFlowLayout.removeAllViews();
                mInfoList.clear();
                mSharedPreferences.edit().clear();
                mSharedPreferences.edit().commit();
                break;

            case R.id.btn_back:
                mFragmentMainActivity.selectionFragment(0);
                break;

            default:
                break;
        }
    }

    /**
    * @description: save search history
    */
    public void saveInfo() {

        String searchContent = mEtSearchContent.getText().toString();

        boolean isSame = false;

        if (mInfoList.isEmpty()) {
            mInfoList.add(searchContent);
        } else {
            for (int i = 0; i < mInfoList.size(); i++) {
                if ((mInfoList.get(i).equals(searchContent))) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                mInfoList.add(searchContent);
            }
        }

        if (mInfoList.size() > CYCLE_TIME) {
            mInfoList.remove(0);
        }

    }

    /**
    * @description: get search history from sharedPreferences
    */
    public List<String> getInfo() {

        mSharedPreferences = getActivity().getSharedPreferences("information", MODE_PRIVATE);

        String json = mSharedPreferences.getString("searchContent", null);

        if (json != null) {
            Gson gson = new Gson();
            mSearchHistory = gson.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        }

        return mSearchHistory;

    }

    /**
    * @description: show search history information
    */
    public void showInfo() {

        mFlowLayout.removeAllViews();

        for (int i = 0; i < mInfoList.size(); i++) {

            if (mInfoList.get(i) != null) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.search_history_item, null);

                final TextView textSearch = v.findViewById(R.id.tv_search);
                textSearch.setText(mInfoList.get(i));

                final ImageView ivSearchDelete = v.findViewById(R.id.iv_search_delete);

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
                            View v = mFlowLayout.getChildAt(i);
                            ImageView ivSearchDelete = v.findViewById(R.id.iv_search_delete);
                            ivSearchDelete.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });

                final int finalI = i;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLvSearchResult.setVisibility(View.VISIBLE);
                        mSearchList = searchResult(mInfoList.get(finalI));
                        mSearchAdapter = new SearchAdapter(mSearchList, mContext);
                        mLvSearchResult.setAdapter(mSearchAdapter);
                    }
                });

                ivSearchDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mInfoList.remove(finalI);
                        showInfo();
                    }
                });
                mFlowLayout.addView(v);
            }
        }
    }

    /**
    * @description: search program
    */
    public List<Program> searchResult(String search) {
        for (int i = 0; i < mProgramList.size(); i++) {
            if (mProgramList.get(i).getProgramName().trim().contains(search) || String.valueOf(mProgramList.get(i).getProgramNum()).trim().contains(search)) {
                mSearchList.add(mProgramList.get(i));
            }
        }
        if (mSearchList.isEmpty()) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.no_search), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return mSearchList;
    }

    @Override
    public void onStop() {

        super.onStop();

        Gson gson = new Gson();
        String searchContent = gson.toJson(mInfoList);

        mSharedPreferences = getActivity().getSharedPreferences("information", MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("searchContent", searchContent);
        editor.commit();
    }
}
