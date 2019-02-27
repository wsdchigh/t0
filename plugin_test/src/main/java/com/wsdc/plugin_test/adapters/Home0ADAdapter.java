package com.wsdc.plugin_test.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wsdchigh.g_a_rely.adapters.CommonVPAdapter;
import com.wsdchigh.g_a_rely.beans.GoodsBean;
import com.wsdchigh.g_a_rely.views.XImageView;

public class Home0ADAdapter extends CommonVPAdapter<GoodsBean> {

    public Home0ADAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(ViewGroup container, Context context, GoodsBean bean) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView iv = new XImageView(context);
        Glide.with(context).asBitmap().load("https://cbu01.alicdn.com/img/back_ibank/2019/420/196/12691024_96354.jpg").into(iv);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return iv;
    }
}
