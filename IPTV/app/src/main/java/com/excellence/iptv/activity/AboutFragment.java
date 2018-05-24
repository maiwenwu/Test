package com.excellence.iptv.activity;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.excellence.iptv.R;

/**
 * @author maiwenwu
 * @date 2018/04/08
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private FragmentMainActivity mFragmentMainActivity = null;
    private ImageView mIvBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        mFragmentMainActivity = (FragmentMainActivity) getActivity();
        mIvBack = view.findViewById(R.id.iv_back);

        mIvBack.setOnClickListener(this);

        return view ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back :
                mFragmentMainActivity.selectionFragment(0);
                break;

            default:
                break;
        }
    }
}
