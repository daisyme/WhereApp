package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
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
public class LoginActivity extends Activity {
    EditText pswtext, usertext;
    Button login,signup;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                try{
                    JSONObject data = new JSONObject((String)msg.obj);
                    gogogo(data.getInt("id"), data.getString("email"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                wrong();
            }
        }
    };

    private void gogogo(int id, String email){
        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
//        intent.setClass(LoginActivity.this,MainActivity.class);
        intent.setAction("Main");
        intent.putExtra("id",id);
        intent.putExtra("username",usertext.getText().toString());
        intent.putExtra("email",email);
        startActivity(intent);
    }

    private void wrong(){
        Toast.makeText(getApplicationContext(), "Username or password wrong!", Toast.LENGTH_LONG).show();
    }

    public int attemptlogin(){
        final String username = usertext.getText().toString();
        final String password = pswtext.getText().toString();
        if (username.isEmpty()){
            return -1;
        }
        if (password.isEmpty()){
            return -2;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"login");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    JSONObject params = new JSONObject();
                    params.put("username",username);
                    params.put("password",password);
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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        usertext = (EditText) this.findViewById(R.id.username);
        pswtext = (EditText) this.findViewById(R.id.password);

        login = (Button) this.findViewById(R.id.email_sign_in_button);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attemptlogin() == 0) {

                }else{
                    if (attemptlogin() == -1){
                        Toast.makeText(getApplicationContext(), "Lost username", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Lost password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        signup = (Button) this.findViewById(R.id.sign_up);
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
