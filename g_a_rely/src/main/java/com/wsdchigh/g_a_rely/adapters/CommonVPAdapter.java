package com.wsdchigh.g_a_rely.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonVPAdapter<T> extends PagerAdapter {
    private List<T> data;
    private Context context;
    private OnItemClickListener<T> onItemClickListener;
    private boolean allowMax = false;

    /*
     *  允许无限切换
     */
    public void allowMax(boolean value){
        allowMax = value;
    }

    public CommonVPAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<T> data){
        if(data == null || data.size() == 0){
            return;
        }
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        if(data == null || data.size() == 0){
            return 0;
        }
        return allowMax?Integer.MAX_VALUE:data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int len = data.size();
        final T t = data.get((position + len) % len);

        View view = getView(container, context, t);
        if(view.getParent() == null){
            container.addView(view);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.click(t);
                }
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(object instanceof View){
            container.removeView((View) object);
        }
    }

    public abstract View getView(ViewGroup container,Context context,T t);

    public interface OnItemClickListener<T>{
        void click(T t);
    }

    /*
     *  如果有事件发生在vp上面
     */
    public interface OnFingerToucherListener{
        void down();
        void up();
    }
}
