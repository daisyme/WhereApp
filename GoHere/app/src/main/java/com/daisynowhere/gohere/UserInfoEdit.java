package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
public class UserInfoEdit extends Activity {

    TextView username,email;
    EditText gender,birth,province,city,desc;
    Button edit,cancel;
    private int id;
    private String user,em;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                success();
            }else{
                back();
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.user_info_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        user = bundle.getString("username");
        em = bundle.getString("email");

        username = (TextView)this.findViewById(R.id.username);
        username.setText(user);
        email = (TextView)this.findViewById(R.id.email);
        email.setText(em);
        gender = (EditText)this.findViewById(R.id.user_gender);
        birth = (EditText)this.findViewById(R.id.user_birth);
        province = (EditText)this.findViewById(R.id.user_province);
        city = (EditText)this.findViewById(R.id.user_city);
        desc = (EditText)this.findViewById(R.id.user_desc);
        edit = (Button)this.findViewById(R.id.user_edit);
        cancel = (Button)this.findViewById(R.id.user_edit_cancel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(baseurl+"profile/"+id);
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("PUT");
                            JSONObject params = new JSONObject();
                            params.put("email",email.getText().toString());
                            params.put("city",city.getText().toString());
                            params.put("province",province.getText().toString());
                            params.put("desc",desc.getText().toString());
                            params.put("gender",gender.getText().toString());
                            params.put("date_of_birth",birth.getText().toString());
                            httpURLConnection.connect();
                            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                            dataOutputStream.writeBytes(params.toString());
                            dataOutputStream.flush();
                            dataOutputStream.close();
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
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void success(){
        Toast.makeText(getApplicationContext(), "Edit Success!", Toast.LENGTH_LONG).show();
        back();
    }

    private void back(){
        this.finish();
    }

}
