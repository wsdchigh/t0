package com.wsdc.plugin_test.plugin.home.home0;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.myfresh.FreshListener;
import com.wsdc.myfresh.ICueDo;
import com.wsdc.myfresh.v1.IFrame;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.adapters.Home0LvAdapter;
import com.wsdc.plugin_test.cue.DefaultICueDo1;
import com.wsdchigh.g_a_rely.adapters.CommonLVAdapter;
import com.wsdchigh.g_a_rely.views.XListView;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/home/home0",sign=PluginSign.VIEW_HOLDER)
public class Home0ViewHolder extends AbstractIViewHolder<Fragment> {
    IFrame frame;
    XListView lv;
    ViewGroup headerContainer;
    CommonLVAdapter adapter;

    View headerView0;

    //  ListView中持有vp，需要提取到出来

    private List<String> headers = new LinkedList<>();
    private List<String> contents = new LinkedList<>();
    private String tail = "abcd";

    {
        headers.add("1");

        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
        contents.add("2");
    }

    public Home0ViewHolder(IPlugin<Fragment, Integer> plugin) {
        super(plugin);
    }

    @Override
    public View install(Context context, Fragment fragment, ViewGroup parent) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.home_home0,parent,false);
        return rootView;
    }

    @Override
    public void notify0(int type, Integer key, IData iData) {
        if(type == IData.TYPE_INIT && iData == plugin.data()){
            initData();
        }
    }

    private void initData() {

    }

    @Override
    public void init(Context context) {
        frame = rootView.findViewById(R.id.test_home_home0_fresh);
        lv = rootView.findViewById(R.id.test_home_home0_lv);
        headerContainer = rootView.findViewById(R.id.test_home_home0_header_bar_container);

        frame.executor().addFreshListener(new FreshListener() {
            @Override
            public void pullToFresh() {
                plugin.handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.complete();
                    }
                },3000);

            }

            @Override
            public void loadMore() {

            }
        });
        ICueDo header = frame.header();
        if(header instanceof DefaultICueDo1){
            ((DefaultICueDo1) header).v = headerContainer;
        }


        adapter = new Home0LvAdapter(context);
        lv.setAdapter(adapter);
        adapter.set(headers,contents,tail);
        headerView0 = adapter.getHeaderView();

        lv.setPrepareLoading(new XListView.OnPrepareLoading() {
            @Override
            public void prepare() {
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");
                contents.add("22");

                adapter.setData0(contents);
            }
        });
    }

    @Override
    public void exit(Context context) {

    }
}
