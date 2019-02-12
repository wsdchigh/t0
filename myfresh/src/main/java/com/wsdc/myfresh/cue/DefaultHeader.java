package com.wsdc.myfresh.cue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsdc.myfresh.ICueDo;
import com.wsdc.myfresh.R;

/**
 * Created by wsdchigh on 2018/1/16.
 */

public class DefaultHeader implements ICueDo {
    private View view;
    private TextView tv;

    public DefaultHeader(Context context, ViewGroup parent){
        view = LayoutInflater.from(context).inflate(R.layout.cue_header_default,parent,false);
        tv = view.findViewById(R.id.cue_header_default_tv);

    }

    @Override
    public void pullMoreToFresh() {
        tv.setText("继续下拉刷新");
        tv.invalidate();
    }

    @Override
    public void releaseToFresh() {
        tv.setText("释放刷新");
        tv.invalidate();
    }

    @Override
    public boolean highLevelFresh() {
        return false;
    }

    @Override
    public void loading() {
        tv.setText("加载中");
        tv.invalidate();
    }

    @Override
    public void complete() {
        tv.setText("完成");
        tv.invalidate();
    }

    @Override
    public void cancel() {
        tv.setText("取消");
    }

    @Override
    public void frameOffset(int x, int y) {

    }

    @Override
    public void offsetSelf(int x, int y) {

    }

    @Override
    public void close() {

    }

    @Override
    public View getView() {
        return view;
    }
}
