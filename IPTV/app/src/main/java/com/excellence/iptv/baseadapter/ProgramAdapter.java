package com.excellence.iptv.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.excellence.iptv.R;
import com.excellence.iptv.bean.Program;

import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/9
 */

public class ProgramAdapter extends BaseAdapter {

    private List<Program> mProgramList ;
    private Context mContext;

    public ProgramAdapter(List<Program> program , Context context) {

        this.mProgramList = program;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mProgramList.size();
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(mContext).inflate(R.layout.live_program_list_fragment,viewGroup,false);

        TextView tvProgramNumber = view.findViewById(R.id.tv_program_number);
        TextView tvProgramName = view.findViewById(R.id.tv_program_name);
        TextView tvProgramTime = view.findViewById(R.id.tv_program_time);
        TextView tvProgramEpg = view.findViewById(R.id.tv_program_epg);
        final ImageView mIvAddFavorite = view.findViewById(R.id.iv_add_favorite);
        if (mProgramList.get(position).isFavorite()){
            mIvAddFavorite.setImageResource(R.drawable.icon_fav);
        }else {
            mIvAddFavorite.setImageResource(R.drawable.icon_addfav);
        }

        tvProgramNumber.setText(mProgramList.get(position).getProgramNum() + "");
        tvProgramName.setText(mProgramList.get(position).getProgramName());
        tvProgramTime.setText(mProgramList.get(position).getProgramTime());
        tvProgramEpg.setText(mProgramList.get(position).getEpg());

        mIvAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mProgramList.get(position).isFavorite()){
                    mIvAddFavorite.setImageResource(R.drawable.icon_addfav);
                    mProgramList.get(position).setFavorite(false);
                }else {
                    mIvAddFavorite.setImageResource(R.drawable.icon_fav);
                    mProgramList.get(position).setFavorite(true);
                }
            }
        });
        return view;
    }
}
