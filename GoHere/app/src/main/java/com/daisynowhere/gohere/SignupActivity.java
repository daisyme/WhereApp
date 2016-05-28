package com.daisynowhere.gohere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SignupActivity extends Activity {
    EditText usertext=null;
    EditText emailtext=null;
    EditText pwdtext=null;
    EditText repwdtext=null;
    Button signup=null;
    Button cancel=null;

    public int attemptsignup(){
        String username = usertext.getText().toString();
        String email = emailtext.getText().toString();
        String password = pwdtext.getText().toString();
        String repassword = repwdtext.getText().toString();
        if (username.isEmpty()){
            return -1;
        }
        if (email.isEmpty()){
            return -2;
        }
        if (password.isEmpty() || repassword.isEmpty()){
            return -3;
        }
        if (password.compareTo(repassword) != 0){
            return -4;
        }
        return 0;
    }

    public void back(){
        this.finish();
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signup);

        usertext = (EditText) this.findViewById(R.id.login_regist_count);
        emailtext = (EditText) this.findViewById(R.id.login_regist_yzm);
        pwdtext = (EditText) this.findViewById(R.id.login_regist_pwd);
        repwdtext = (EditText) this.findViewById(R.id.login_regist_repwd);

        signup = (Button) this.findViewById(R.id.login_regist_completeregist);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = attemptsignup();
                switch (state){
                    case -1 : Toast.makeText(getApplicationContext(), "Lost username", Toast.LENGTH_LONG).show(); break;
                    case -2 : Toast.makeText(getApplicationContext(), "Lost email", Toast.LENGTH_LONG).show(); break;
                    case -3 : Toast.makeText(getApplicationContext(), "Lost password", Toast.LENGTH_LONG).show(); break;
                    case -4 : Toast.makeText(getApplicationContext(), "Passwords are not equal", Toast.LENGTH_LONG).show(); break;
                    case 0 :
                        Toast.makeText(getApplicationContext(), "Sign up success!", Toast.LENGTH_LONG).show();
                        back();
                }
            }
        });

        cancel = (Button) this.findViewById(R.id.login_regist_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
}
