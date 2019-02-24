package com.wsdc.plugin_test.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsdc.plugin_test.R;
import com.wsdchigh.g_a_rely.adapters.CommonLVAdapter;

import java.util.List;

public class Home0LvAdapter extends CommonLVAdapter {
    public Home0LvAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getHeaderView(Context context, int position, Object o,ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_home0_lv_header, parent, false);
        return view;
    }

    @Override
    protected VH getDataView(int type, List data0, int position, Context context,ViewGroup parent) {
        return new VH0(context);
    }

    @Override
    protected View getTailView(Context context, Object o,ViewGroup parent) {
        TextView tv = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,60);
        tv.setLayoutParams(params);
        tv.setText("加载中...");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    public static class VH0 implements VH{
        private Context context;
        private TextView tv;

        public VH0(Context context) {
            this.context = context;
            tv = new TextView(context);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv.setLayoutParams(params);
            tv.setText("内容主体");
        }

        @Override
        public void install0(Object y1) {

        }

        @Override
        public void install1(Object y1, Object y2) {

        }

        @Override
        public View getRootView() {
            return tv;
        }
    }
}
