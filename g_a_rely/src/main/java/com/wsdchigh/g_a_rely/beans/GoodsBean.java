package com.wsdchigh.g_a_rely.beans;

import java.util.List;

/*
 *  关于商品的信息
 */
public class GoodsBean {
    //  商品ID
    public Integer goodsID;

    //  商品名字
    public String name;

    //  主图
    public String imageSmall;

    //  广告图片
    public String adImage;

    //  图片详情界面
    public List<String> imageDetails;

    //  当前售价 折扣之后的价格
    public Float salePrice;

    //  原本的价格           关于价格信息均只提供参考，所有的信息均已规格信息为主
    public Float originPrice;

    //  折扣 9.3折?
    public Float discount;

    //  描述
    public String describe;

    //  商家名字
    public String storeName;

    //  商家电话
    public String storeTel;

    //  商家的图标
    public String storeIcon;

    //  库存
    public Integer count;

    //  销量
    public Integer saleCount;

    //  最近一个月销量
    public Integer saleCountRecent;


}
