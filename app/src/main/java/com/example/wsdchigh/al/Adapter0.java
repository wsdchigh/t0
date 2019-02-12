package com.example.wsdchigh.al;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter0 extends BaseAdapter {
    private List<String> data;
    private Context context;

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Adapter0(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if(data == null){
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        tv.setText(data.get(position));
        tv.setLayoutParams(params);
        return tv;
    }
}
