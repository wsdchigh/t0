package com.example.wsdchigh.al;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wsdc.myfresh.FreshListener;
import com.wsdc.myfresh.v1.IFrame;

import java.util.ArrayList;
import java.util.List;

public class ViewHolder0 {
    Context context;
    View root;
    ListView lv;
    IFrame frame;

    Adapter0 adapter ;
    List<String> data = new ArrayList<>();
    int i= 0;

    Handler handler = new Handler();

    public ViewHolder0(Context context) {
        this.context = context;
        root = LayoutInflater.from(context).inflate(R.layout.activity_main,null);
    }

    public void install(){
        lv = root.findViewById(R.id.lv);
        frame = root.findViewById(R.id.frame);
        adapter = new Adapter0(context);
        lv.setAdapter(adapter);


        for (int i1 = 0; i1 < 18; i1++) {
            data.add("ksv"+i++);
        }

        adapter.setData(data);

        frame.executor().addFreshListener(new FreshListener() {
            @Override
            public void pullToFresh() {
                Log.d("wsdc", "pull to fresh");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        for (int i1 = 0; i1 < 18; i1++) {
                            data.add("ksv"+i++);
                        }

                        adapter.setData(data);
                        frame.complete();
                    }
                },6000);
            }

            @Override
            public void loadMore() {
                Log.d("wsdc", "load more");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        for (int i1 = 0; i1 < 18; i1++) {
                            data.add("ksv"+i++);
                        }

                        adapter.setData(data);
                        frame.complete();
                    }
                },6000);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wsdc", "点击"+position);
            }
        });
    }

    public View getView(){
        return root;
    }



}
