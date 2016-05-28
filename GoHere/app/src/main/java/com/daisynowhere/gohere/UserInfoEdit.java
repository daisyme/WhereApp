package com.daisynowhere.gohere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/24.
 */
public class UserInfoEdit extends Activity {

    TextView username,email;
    EditText gender,birth,province,city,desc;
    Button edit,cancel;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.user_info_edit);
        username = (TextView)this.findViewById(R.id.username);
        email = (TextView)this.findViewById(R.id.email);
        gender = (EditText)this.findViewById(R.id.user_gender);
        birth = (EditText)this.findViewById(R.id.user_birth);
        province = (EditText)this.findViewById(R.id.user_province);
        city = (EditText)this.findViewById(R.id.user_city);
        desc = (EditText)this.findViewById(R.id.user_desc);
        edit = (Button)this.findViewById(R.id.user_edit);
        cancel = (Button)this.findViewById(R.id.user_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Edit Success!", Toast.LENGTH_LONG).show();
                back();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back(){
        this.finish();
    }

}
