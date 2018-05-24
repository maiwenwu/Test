package com.excellence.iptv.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.excellence.iptv.R;
import com.excellence.iptv.baseadapter.FavoriteAdapter;
import com.excellence.iptv.bean.Program;
import java.util.ArrayList;
import java.util.List;


/**
 * @author PC-001
 * @date 2018/04/11
 */
public class FavoriteFragment extends Fragment {

    private FragmentMainActivity mFragmentMainActivity = null;
    private List<Program> mFavoriteProgramList = new ArrayList<>();
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        View view1 = inflater.inflate(R.layout.fragment_main_activity,container,false);
        mFragmentMainActivity = (FragmentMainActivity) getActivity();

        ImageView ivFavoriteBack = view.findViewById(R.id.iv_favorite_back);

        Context context = getActivity();
        mFavoriteProgramList.clear();

        RecyclerView recycleListView = view.findViewById(R.id.rl_favorite_program);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recycleListView.setLayoutManager(staggeredGridLayoutManager);

        List<Program> programList = mFragmentMainActivity.programList;

         /*
         *   get favorite program information
         */
        for (int i = 0; i < programList.size() ; i ++) {
            if (programList.get(i).isFavorite()){
                mFavoriteProgramList.add(programList.get(i));
            }
        }

        if (mFavoriteProgramList.isEmpty()){
            Toast toast = Toast.makeText(getActivity(),getString(R.string.no_favorite),Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else {
            final FavoriteAdapter favoriteAdapter = new FavoriteAdapter(mFavoriteProgramList, context);
            recycleListView.setAdapter(favoriteAdapter);
        }

        ivFavoriteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentMainActivity.selectionFragment(0);
            }
        });

        return view;
    }

}
