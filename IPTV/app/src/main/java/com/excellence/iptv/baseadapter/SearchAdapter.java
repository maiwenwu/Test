package com.excellence.iptv.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.excellence.iptv.R;
import com.excellence.iptv.bean.Program;

import java.util.List;

/**
 *
 * @author maiwenwu
 * @date 2018/4/16
 */

public class SearchAdapter extends BaseAdapter {

    private List<Program> mSearchList ;
    private Context mContext;

    public SearchAdapter(List<Program> program , Context context) {

        this.mSearchList = program;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mSearchList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item,viewGroup,false);

        TextView tvSearchNumber = view.findViewById(R.id.tv_search_program_number);
        TextView tvSearchName = view.findViewById(R.id.tv_search_program_name);
        TextView tvSearchTime = view.findViewById(R.id.tv_search_program_time);
        TextView tvSearchEpg = view.findViewById(R.id.tv_search_program_epg);

        tvSearchNumber.setText(mSearchList.get(position).getProgramNum() + "");
        tvSearchName.setText(mSearchList.get(position).getProgramName());
        tvSearchTime.setText(mSearchList.get(position).getProgramTime());
        tvSearchEpg.setText(mSearchList.get(position).getEpg());

        return view;
    }
}
