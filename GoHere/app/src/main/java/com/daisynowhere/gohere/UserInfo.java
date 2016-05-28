package com.daisynowhere.gohere;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/24.
 */
public class UserInfo extends Activity {

    TextView username,email,gender,birth,province,city,desc;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.user_info);
        username = (TextView)this.findViewById(R.id.username);
        email = (TextView)this.findViewById(R.id.email);
        gender = (TextView)this.findViewById(R.id.user_gender);
        birth = (TextView)this.findViewById(R.id.user_birth);
        province = (TextView)this.findViewById(R.id.user_province);
        city = (TextView)this.findViewById(R.id.user_city);
        desc = (TextView)this.findViewById(R.id.user_desc);
    }
}
