package com.wsdchigh.g_a_rely.views;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wsdc.g_j_0.IContainer0;

import java.util.LinkedList;
import java.util.List;

public class XListView extends ListView {
    long t1 = 0l;
    OnScrollListener1 l = new OnScrollListener1();
    OnScrollListener inner = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int first = getFirstVisiblePosition();
            int last = first + getChildCount();
            int total = getCount();

            if(total <= 4){
                if(prepareLoading != null){
                    long t2 = System.currentTimeMillis();
                    if(t2 - t1 > 3000){
                        t1 = t2;
                        prepareLoading.prepare();
                    }
                }
                return;
            }

            /*
             *  3s最多触发一次
             */
            if(last >= total - 4){
                if(prepareLoading != null){
                    long t2 = System.currentTimeMillis();
                    if(t2 - t1 > 3000){
                        t1 = t2;
                        prepareLoading.prepare();
                    }
                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    private OnPrepareLoading prepareLoading;

    public void setPrepareLoading(OnPrepareLoading prepareLoading) {
        this.prepareLoading = prepareLoading;
    }

    public XListView(Context context) {
        this(context,null);
    }

    public XListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        l.register(inner);
        setOnScrollListener0(l);
    }


    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if(Looper.myLooper() != Looper.getMainLooper()){
            return;
        }

        this.l.register(l);
    }

    public void setOnScrollListener0(OnScrollListener l) {
        super.setOnScrollListener(l);
    }

    private static class OnScrollListener1 implements OnScrollListener,IContainer0<OnScrollListener,Integer> {
        List<OnScrollListener> listeners = new LinkedList<>();

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            for (OnScrollListener listener : listeners) {
                listener.onScrollStateChanged(view,scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            for (OnScrollListener listener : listeners) {
                listener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
            }
        }

        @Override
        public void register(OnScrollListener onScrollListener) {
            listeners.add(onScrollListener);
        }

        @Override
        public void unregister(OnScrollListener onScrollListener) {
            listeners.remove(onScrollListener);
        }

        @Override
        public void notify0(int type, Integer key) {

        }
    }

    int maxToPrepare = 4;
    public void setMaxToPrepare(int maxToPrepare){
        this.maxToPrepare = maxToPrepare;
    }

    /*
     *  预先刷新
     *  <li>    如果总的长度快要到了，那么需要发送预先刷新信号
     */
    public interface OnPrepareLoading{
        void prepare();
    }
}
