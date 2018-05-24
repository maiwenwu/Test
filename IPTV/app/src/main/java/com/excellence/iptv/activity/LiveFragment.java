package com.excellence.iptv.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.excellence.iptv.R;
import com.excellence.iptv.baseadapter.ProgramAdapter;
import com.excellence.iptv.bean.Program;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/04/08
 */
public class LiveFragment extends Fragment implements View.OnClickListener {

    public List<Program> mProgramList = new ArrayList<>();
    private List<Program> mFavorites = new ArrayList<>();
    private FragmentMainActivity mFragmentMainActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.live_fragment, container, false);
        mFragmentMainActivity = (FragmentMainActivity) getActivity();

        ListView lvProgramMsg = view.findViewById(R.id.lv_program_meg);
        View searchLinearLayout = view.findViewById(R.id.ll_search);
        ImageView ivFavorite = view.findViewById(R.id.iv_favorite);

        Context context = getActivity();

        mProgramList = mFragmentMainActivity.programList;
        ProgramAdapter programAdapter = new ProgramAdapter(mProgramList, context);
        lvProgramMsg.setAdapter(programAdapter);

        searchLinearLayout.setOnClickListener(this);
        ivFavorite.setOnClickListener(this);

        lvProgramMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("program", gson.toJson(mProgramList.get(i)));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_search:
                mFragmentMainActivity.selectionFragment(2);
                break;

            case R.id.iv_favorite:
                mFragmentMainActivity.selectionFragment(3);
                break;

            default:
                break;
        }
    }

}
