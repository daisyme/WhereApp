package com.daisynowhere.gohere;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Administrator on 2016/5/23.
 */
public class PoiInfo extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       // this.setContentView(R.layout.poi_info);
        SlidingMenu slidingMenu = new SlidingMenu(this, LayoutInflater.from(this).inflate(R.layout.poi_info,null),LayoutInflater.from(this).inflate(R.layout.activity_user_profile,null));
        this.setContentView(slidingMenu);
    }
}
