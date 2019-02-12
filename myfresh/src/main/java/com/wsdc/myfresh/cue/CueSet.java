package com.wsdc.myfresh.cue;

import android.view.View;

import com.wsdc.myfresh.IContainer;
import com.wsdc.myfresh.ICueDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsdchigh on 2018/1/16.
 */

public class CueSet implements ICueDo,IContainer<ICueDo> {
    private List<ICueDo> resultSet = new ArrayList<>();

    @Override
    public void pullMoreToFresh() {
        for (ICueDo cue : resultSet) {
            cue.pullMoreToFresh();
        }
    }

    @Override
    public void releaseToFresh() {
        for (ICueDo cue : resultSet) {
            cue.releaseToFresh();
        }
    }

    @Override
    public boolean highLevelFresh() {
        boolean result = false;
        for (ICueDo cue : resultSet) {
            if(cue.highLevelFresh()){
                result = true;
            }
        }
        return result;
    }

    @Override
    public void loading() {
        for (ICueDo cue : resultSet) {
            cue.loading();
        }
    }

    @Override
    public void complete() {
        for (ICueDo cue : resultSet) {
            cue.complete();
        }
    }

    @Override
    public void cancel() {
        for (ICueDo cueDo : resultSet) {
            cueDo.cancel();
        }
    }

    @Override
    public void frameOffset(int x, int y) {
        for (ICueDo cue : resultSet) {
            cue.frameOffset(x,y);
        }
    }

    @Override
    public void offsetSelf(int x, int y) {
        for (ICueDo cue : resultSet) {
            cue.offsetSelf(x,y);
        }
    }

    @Override
    public void close() {
        for (ICueDo cueDo : resultSet) {
            cueDo.close();
        }
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void add(ICueDo cue) {
        if(!resultSet.contains(cue)){
            resultSet.add(cue);
        }
    }

    @Override
    public void remove(ICueDo cue) {
        resultSet.remove(cue);
    }
}
