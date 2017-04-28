package com.jzbwlkj.zpyx.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/11.
 */

public class HomeBannerBean {

    /**
     * status : 1
     * msg : 获取成功
     * data : [{"ad_id":71,"ad_position":0,"ad_pic":"upload/adspic/2017-04/58e33ea83a767.png","ad_name":"顶级睡眠日","ad_type":1,"ad_url":"1","start_date":1491235200,"end_date":1493481600,"ad_sort":1,"create_time":1491287733}]
     */

    /**
     * ad_id : 71
     * ad_position : 0
     * ad_pic : upload/adspic/2017-04/58e33ea83a767.png
     * ad_name : 顶级睡眠日
     * ad_type : 1
     * ad_url : 1
     * start_date : 1491235200
     * end_date : 1493481600
     * ad_sort : 1
     * create_time : 1491287733
     */

    private int ad_id;
    private int ad_position;
    private String ad_pic;
    private String ad_name;
    private int ad_type;
    private String ad_url;
    private int start_date;
    private int end_date;
    private int ad_sort;
    private int create_time;

    public int getAd_id() {
        return ad_id;
    }

    public void setAd_id(int ad_id) {
        this.ad_id = ad_id;
    }

    public int getAd_position() {
        return ad_position;
    }

    public void setAd_position(int ad_position) {
        this.ad_position = ad_position;
    }

    public String getAd_pic() {
        return ad_pic;
    }

    public void setAd_pic(String ad_pic) {
        this.ad_pic = ad_pic;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public int getStart_date() {
        return start_date;
    }

    public void setStart_date(int start_date) {
        this.start_date = start_date;
    }

    public int getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
    }

    public int getAd_sort() {
        return ad_sort;
    }

    public void setAd_sort(int ad_sort) {
        this.ad_sort = ad_sort;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
