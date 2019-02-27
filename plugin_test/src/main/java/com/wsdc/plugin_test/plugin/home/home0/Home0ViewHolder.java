package com.wsdc.plugin_test.plugin.home.home0;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.wsdc.g_a_0.annotation.APlugin;
import com.wsdc.g_a_0.annotation.PluginSign;
import com.wsdc.g_a_0.plugin.IData;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.plugin.inner.AbstractIViewHolder;
import com.wsdc.myfresh.FreshListener;
import com.wsdc.myfresh.ICueDo;
import com.wsdc.myfresh.v1.IFrame;
import com.wsdc.plugin_test.R;
import com.wsdc.plugin_test.adapters.Home0ADAdapter;
import com.wsdc.plugin_test.adapters.Home0LvAdapter;
import com.wsdc.plugin_test.cue.DefaultICueDo1;
import com.wsdchigh.g_a_rely.adapters.CommonLVAdapter;
import com.wsdchigh.g_a_rely.adapters.CommonVPAdapter;
import com.wsdchigh.g_a_rely.beans.GoodsBean;
import com.wsdchigh.g_a_rely.utis.USpan;
import com.wsdchigh.g_a_rely.views.XIndicate;
import com.wsdchigh.g_a_rely.views.XListView;
import com.wsdchigh.g_a_rely.views.XViewPager;

import java.util.LinkedList;
import java.util.List;

@APlugin(key="/test/home/home0",sign=PluginSign.VIEW_HOLDER)
public class Home0ViewHolder extends AbstractIViewHolder<Fragment> {
    IFrame frame;
    XListView lv;
    ViewGroup headerContainer;
    CommonLVAdapter lvAdapter;
    CommonVPAdapter vpAdapter;

    View headerView0;
    XViewPager vp;
    XIndicate indicates;

    ViewFlipper vf;

    LinearLayout news,perfect,hots;

    //  ListView中持有vp，需要提取到出来

    private List<String> headers = new LinkedList<>();
    private List<String> contents = new LinkedList<>();
    private String tail = "abcd";


    private List<GoodsBean> vpListData = new LinkedList<>();

    private List vfData = new LinkedList();
    private List<View> vfChildViews = new LinkedList<>();

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

        vpListData.add(new GoodsBean());
        vpListData.add(new GoodsBean());
        vpListData.add(new GoodsBean());
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


        lvAdapter = new Home0LvAdapter(context);
        lv.setAdapter(lvAdapter);
        lvAdapter.set(headers,contents,tail);

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

                lvAdapter.setData0(contents);
            }
        });

        lvAdapter.setHeaderViewCreate(new CommonLVAdapter.OnHeaderViewCreate() {
            @Override
            public void headerViewCreate(View v) {
                //  初始化数据
                if(headerView0 == null){
                    headerView0 = v;
                    vp = headerView0.findViewById(R.id.test_home_home0_vp_0);
                    indicates = headerView0.findViewById(R.id.test_home_home0_indicate);
                    vf = headerView0.findViewById(R.id.test_home_home0_vf_0);
                    news = headerView0.findViewById(R.id.test_home_home0_lin_news);
                    perfect = headerView0.findViewById(R.id.test_home_home0_lin_perfect);
                    hots = headerView0.findViewById(R.id.test_home_home0_lin_hots);


                    vpAdapter = new Home0ADAdapter(context);
                    vp.setAdapter(vpAdapter);
                    vpAdapter.allowMax(true);
                    vpAdapter.setData(vpListData);
                    indicates.setCount(vpListData.size());
                    indicates.setNow(0);
                    vp.addOnPageChangeListener(indicates);
                    vp.init(plugin.handler());

                    //  初始化vf数据
                    for (int i = 0; i < 4; i++) {
                        TextView tv = new TextView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(params);
                        SpannableString ss = USpan.getInstance().colorSpan("好汉" + i, 0, 2, Color.RED);
                        Drawable d = context.getResources().getDrawable(R.mipmap.ic_launcher);
                        d.setBounds(0,0,32,32);
                        SpannableString ss1 = USpan.getInstance().imageSpan("功夫熊猫表情", 4, 6, d);
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        SpannableStringBuilder ssb = builder.append(ss).append(ss1);

                        tv.setText(ssb);
                        tv.setGravity(Gravity.CENTER_VERTICAL);
                        vf.addView(tv);
                    }

                    vf.setAutoStart(true);
                    vf.setFlipInterval(5000);
                    Animation in = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
                            Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,0);
                    in.setDuration(1500);
                    Animation out = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
                            Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1);
                    out.setDuration(1500);
                    vf.setInAnimation(in);
                    vf.setOutAnimation(out);
                    vf.startFlipping();

                    //  初始化新品，精品，热销产品
                    Log.d("wsdc1", "time = "+System.currentTimeMillis());
                    for (int i = 0; i < 3; i++) {
                        TextView tv = new TextView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(params);
                        params.width = 600;
                        //IFrame
                        tv.setText("好汉"+i);
                        if(i % 2 == 0){
                            tv.setBackgroundColor(0xff00ffff);
                        }else{
                            tv.setBackgroundColor(0xffff00ff);
                        }
                        tv.setGravity(Gravity.CENTER_VERTICAL);
                        news.addView(tv);
                    }
                    for (int i = 0; i < 3; i++) {
                        TextView tv = new TextView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(params);
                        tv.setText("好汉"+i);
                        if(i % 2 == 0){
                            tv.setBackgroundColor(0xff00ffff);
                        }else{
                            tv.setBackgroundColor(0xffff00ff);
                        }
                        tv.setGravity(Gravity.CENTER_VERTICAL);
                        perfect.addView(tv);
                    }
                    for (int i = 0; i < 3; i++) {
                        TextView tv = new TextView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(params);
                        tv.setText("好汉"+i);
                        if(i % 2 == 0){
                            tv.setBackgroundColor(0xff00ffff);
                        }else{
                            tv.setBackgroundColor(0xffff00ff);
                        }
                        tv.setGravity(Gravity.CENTER_VERTICAL);
                        hots.addView(tv);
                    }

                    Log.d("wsdc1", "time = "+System.currentTimeMillis());
                }


            }
        });
    }

    @Override
    public void exit(Context context) {
        if(vp != null){
            vp.exit(plugin.handler());
            Log.d("wsdc1", "避免内存泄漏");
        }

        if(vf != null){
            //  vf需要停止播放    避免内存泄漏
            vf.stopFlipping();
        }
    }
}
