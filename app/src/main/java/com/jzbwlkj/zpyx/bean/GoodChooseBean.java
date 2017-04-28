package com.jzbwlkj.zpyx.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 */

public class GoodChooseBean {

    /**
     * status : 1
     * msg : 获取成功
     * data : [{"goods_id":7,"goods_pic":"147592353524384","goods_name":"咖喱果2斤","goods_price":"40.00","goods_source":"30.00"}]
     */

    /**
     * goods_id : 7
     * goods_pic : 147592353524384
     * goods_name : 咖喱果2斤
     * goods_price : 40.00
     * goods_source : 30.00
     */

    private int goods_id;
    private String goods_pic;
    private String goods_name;
    private String goods_price;
    private String goods_source;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_source() {
        return goods_source;
    }

    public void setGoods_source(String goods_source) {
        this.goods_source = goods_source;
    }
}
