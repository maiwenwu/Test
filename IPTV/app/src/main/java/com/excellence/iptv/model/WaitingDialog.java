package com.excellence.iptv.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.excellence.iptv.R;

/**
 * @author maiwenwu
 * @date 2018/4/4
 */

public class WaitingDialog {

    public static Dialog createLoadingDialog(Context context) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_waiting_activity, null);

        LinearLayout layout =  view.findViewById(R.id.dialog_view);

        ImageView ivWaiting =  view.findViewById(R.id.iv_waiting);

        Animation animation = AnimationUtils.loadAnimation(context,            R.anim.rorate);
        ivWaiting.startAnimation(animation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }
}
