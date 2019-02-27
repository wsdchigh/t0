package com.wsdchigh.g_a_rely.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 *  通用的ListView适配器
 *  <li>    尾部添加footView，用于加载更多
 *  <li>    当数据显示的index超过了总数据量的一半，提示刷新  (由ListView自己去实现)
 *
 *  <li>    只有data中的view是浮动的，其他view数量均是固定的
 *          <li>    除了data中的View，其他的View均持有实例   (不缓存)
 *          <li>    因为里面的数据可能比较复杂，省去一些花里胡哨的操作
 */
public abstract class CommonLVAdapter<X,Y,Z> extends BaseAdapter {
    private List<X> headerData;
    private List<Y> data0;
    private Z z;

    private int headerSize = 0;
    private int dataSize = 0;
    private int footSize = 0;

    private Map<Integer,View> headerViewsMap = new TreeMap<>();
    private View tailView;

    private int howMuchInLine = 1;

    private Context context;

    private OnHeaderViewCreate headerViewCreate;

    public void setHeaderViewCreate(OnHeaderViewCreate headerViewCreate) {
        this.headerViewCreate = headerViewCreate;
    }

    public CommonLVAdapter(Context context) {
        this.context = context;
    }

    public void setHeader(List<X> data){
        headerData = data;
        headerSize = headerData.size();
        //headerViewsMap.clear();
        notifyDataSetChanged();
    }

    public void setData0(List<Y> data){
        this.data0 = data;
        dataSize = (this.data0.size()+howMuchInLine-1)/howMuchInLine;
        notifyDataSetChanged();
    }

    public void setFoot(Z z){
        this.z = z;
        footSize = 1;
        tailView = null;
        //notifyDataSetChanged();
    }

    public void set(List<X> xs,List<Y> ys,Z z){
        headerData = xs;
        headerSize = headerData.size();
        headerViewsMap.clear();

        data0 = ys;
        dataSize = (this.data0.size()+howMuchInLine-1)/howMuchInLine;

        this.z = z;
        footSize = 1;
        tailView = null;

        notifyDataSetChanged();
    }

    //  针对data，一行显示的数量，同时是1个或者2个    (暂时只允许这两个值)
    public void setHowMuchInLine(int howMuchInLine) {
        if(howMuchInLine == 1 || howMuchInLine == 2){
            this.howMuchInLine = howMuchInLine;
            dataSize = (this.data0.size()+howMuchInLine-1)/howMuchInLine;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return headerSize+dataSize+footSize;
    }

    @Override
    public Object getItem(int position) {
        if(position < headerSize){
            return headerData.get(position);
        }else if(position < headerSize + dataSize){
            return data0.get(position-headerSize);
        }else{
            return z;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean shouldNotifyHeaderView = false;
        if(position == 0 && headerViewsMap.get(position) == null){
            shouldNotifyHeaderView = true;
        }
        View rtn = null;
        if(position < headerSize){
            rtn = headerViewsMap.get(position);
            if(rtn == null){
                rtn = getHeaderView(context,position,headerData.get(position),parent);
                headerViewsMap.put(position,rtn);
            }
        }else if(position < headerSize + dataSize){
            VH vh = null;
            if(convertView == null){
                vh = getDataView(howMuchInLine,data0,position-headerSize,context,parent);
                vh.getRootView().setTag(vh);
            }else{
                vh = (VH) convertView.getTag();
                if(vh == null){
                    vh = getDataView(howMuchInLine,data0,position-headerSize,context,parent);
                    vh.getRootView().setTag(vh);
                }
            }

            Y y1,y2;
            int p1 = position-headerSize;
            if(howMuchInLine ==2){
                if((p1+1) * howMuchInLine < data0.size()){
                    y1 = data0.get(p1*2);
                    y2 = data0.get(p1*2 + 1);
                }else{
                    y1 = data0.get(p1*2);
                    y2 = null;
                }
                vh.install1(y1,y2);
            }else{
                y1 = data0.get(p1);
                vh.install0(y1);
            }
            rtn = vh.getRootView();
        }else{
            if(tailView == null){
                tailView = getTailView(context,z,parent);
            }
            rtn = tailView;
        }

        //  创建监听
        if(shouldNotifyHeaderView && headerViewCreate != null){
            headerViewCreate.headerViewCreate(headerViewsMap.get(0));
        }
        return rtn;
    }

    protected abstract View getHeaderView(Context context,int position,X x,ViewGroup parent);
    protected abstract VH<Y> getDataView(int type,List<Y> data0,int position,Context context,ViewGroup parent);
    protected abstract View getTailView(Context context,Z z,ViewGroup parent);

    public View getHeaderView(){
        return headerViewsMap.get(0);
    }

    public View getTailView(){
        return tailView;
    }

    public interface VH<Y>{
        public void install0(Y y1);

        public void install1(Y y1,Y y2);

        View getRootView();
    }

    public interface OnHeaderViewCreate{
        void headerViewCreate(View v);
    }
}
