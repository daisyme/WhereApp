package com.daisynowhere.gohere;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                back();
            }else{
                wrong();
            }
        }
    };

    public int attemptsignup(){
        final String username = usertext.getText().toString();
        final String email = emailtext.getText().toString();
        final String password = pwdtext.getText().toString();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"register");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    JSONObject params = new JSONObject();
                    params.put("username",username);
                    params.put("password",password);
                    params.put("email",email);
                    httpURLConnection.connect();
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(params.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    int resultcode = httpURLConnection.getResponseCode();
                    if (resultcode == HttpURLConnection.HTTP_OK) {
                        InputStream is = httpURLConnection.getInputStream();
                        //创建包装流
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        //定义String类型用于储存单行数据
                        String line = null;
                        //创建StringBuffer对象用于存储所有数据
                        StringBuffer sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        Message message = new Message();
                        message.what = 0;
                        message.obj = sb.toString();
                        handler.sendMessage(message);
                    }else{
                        Message message = new Message();
                        message.what = -1;
                        handler.sendMessage(message);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return 0;
    }

    private void wrong(){
        Toast.makeText(getApplicationContext(), "Sign up wrong!", Toast.LENGTH_LONG).show();
    }

    private void back(){
        Toast.makeText(getApplicationContext(), "Sign up success!", Toast.LENGTH_LONG).show();
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
                        break;
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
