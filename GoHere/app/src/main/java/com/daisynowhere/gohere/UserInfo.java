package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/24.
 */
public class UserInfo extends Activity {

    TextView username,email,gender,birth,province,city,desc;
    private int id;
    private String user;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                try{
                    JSONObject data = new JSONObject((String)msg.obj);
                    changeText(data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private void changeText(JSONObject jsonObject){
        try {
            username.setText(user);
            email.setText(jsonObject.getString("email"));
            gender.setText(jsonObject.getString("gender").isEmpty() ? "" : jsonObject.getString("gender"));
            birth.setText(jsonObject.getString("date_of_birth").isEmpty() ? "" : jsonObject.getString("date_of_birth"));
            //province.setText(jsonObject.getString("province"));
            //city.setText(jsonObject.getString("city"));
            desc.setText(jsonObject.getString("desc").isEmpty() ? "" : jsonObject.getString("desc"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.user_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        user = bundle.getString("username");

        username = (TextView)this.findViewById(R.id.username);
        email = (TextView)this.findViewById(R.id.email);
        gender = (TextView)this.findViewById(R.id.user_gender);
        birth = (TextView)this.findViewById(R.id.user_birth);
        province = (TextView)this.findViewById(R.id.user_province);
        city = (TextView)this.findViewById(R.id.user_city);
        desc = (TextView)this.findViewById(R.id.user_desc);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"profile/"+id);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    int resultcode = httpURLConnection.getResponseCode();
                    if (resultcode == HttpURLConnection.HTTP_OK) {
                        InputStream is = httpURLConnection.getInputStream();
                        //������װ��
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        //����String�������ڴ��浥�����
                        String line = null;
                        //����StringBuffer�������ڴ洢�������
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
    }
}
