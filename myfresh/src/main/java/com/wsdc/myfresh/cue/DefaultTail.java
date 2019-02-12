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

public class DefaultTail implements ICueDo {
    private View tail;
    private TextView tv;

    public DefaultTail(Context context, ViewGroup parent){
        tail = LayoutInflater.from(context).inflate(R.layout.cue_tail_default,parent,false);
        tv = tail.findViewById(R.id.cue_tail_default_tv);
    }

    @Override
    public void pullMoreToFresh() {

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

    }

    @Override
    public void cancel() {

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
        return tail;
    }
}
