package com.wsdc.plugin_test.cue;

import android.content.Context;
import android.view.View;

import com.wsdc.myfresh.ICueDo;

public class DefaultICueDo1 implements ICueDo {
    private Context context;
    private View rootView;

    public View v;

    public DefaultICueDo1(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    @Override
    public void pullMoreToFresh() {
        if(v != null){
            v.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void releaseToFresh() {

    }

    @Override
    public boolean highLevelFresh() {
        return false;
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete() {
        if(v != null){
            v.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancel() {
        if(v != null){
            v.setVisibility(View.VISIBLE);
        }
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
        return rootView;
    }
}
