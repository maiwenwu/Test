package com.excellence.iptv.baseadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.excellence.iptv.R;
import com.excellence.iptv.activity.PlayActivity;
import com.excellence.iptv.bean.Program;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author maiwenwu
 * @date 2018/4/17
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Program> mProgramList;
    private Context mContext;
    private boolean isShow = false;

    public FavoriteAdapter(List<Program> program, Context context) {

        this.mProgramList = program;
        this.mContext = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFavoritePic;
        TextView tvFavoriteNum;
        TextView tvFavoriteName;
        View recyclerView;
        ImageView ivFavoriteDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFavoritePic = itemView.findViewById(R.id.iv_favorite_pic);
            ivFavoriteDelete = itemView.findViewById(R.id.iv_favorite_delete);
            tvFavoriteNum = itemView.findViewById(R.id.tv_favorite_num);
            tvFavoriteName = itemView.findViewById(R.id.tv_favorite_name);
            recyclerView = itemView;
            tvFavoriteName.setSelected(true);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_activity,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.ivFavoritePic.setImageResource(Integer.parseInt(mProgramList.get(position).getFavoritePic()));
        holder.tvFavoriteNum.setText(mProgramList.get(position).getProgramNum() + "");
        holder.tvFavoriteName.setText(mProgramList.get(position).getProgramName());

        holder.ivFavoriteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgramList.get(position).setFavorite(false);
                mProgramList.remove(position);
                isShow = false;
                notifyDataSetChanged();
            }
        });

        if (isShow){
            holder.ivFavoriteDelete.setVisibility(View.VISIBLE);
        }else {
            holder.ivFavoriteDelete.setVisibility(View.INVISIBLE);
        }

        holder.recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isShow =true;
                notifyDataSetChanged();
                return true;
            }
        });

        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("program", gson.toJson(mProgramList.get(position)));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }

}
