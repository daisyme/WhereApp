package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.AccessibleObject;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/23.
 */
public class PoiInfo extends AppCompatActivity {
    //Button back;
    TextView title,desc;
    ImageButton check,like,togo;
    private int uid,pid;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                try{
                    JSONObject jsonObject = new JSONObject((String)msg.obj);
                    title.setText(jsonObject.getString("name"));
                    desc.setText(jsonObject.getString("desc"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private void cancel(){
        this.finish();
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this.setContentView(R.layout.poi_info);
        //SlidingMenu slidingMenu = new SlidingMenu(this, LayoutInflater.from(this).inflate(R.layout.poi_info,null),LayoutInflater.from(this).inflate(R.layout.activity_user_profile,null));
        //this.setContentView(slidingMenu);
        this.setContentView(R.layout.poi_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pid = bundle.getInt("pid");
        uid = bundle.getInt("uid");

        //back = (Button) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        desc = (TextView) this.findViewById(R.id.textView);
        check = (ImageButton) this.findViewById(R.id.check);
        like = (ImageButton) this.findViewById(R.id.like);
        togo = (ImageButton) this.findViewById(R.id.togo);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_input_delete);
        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("�������");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"poi/"+pid);
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

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(baseurl + "checkin/" + uid);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("PUT");
                            JSONObject params = new JSONObject();
                            params.put("pid", pid);
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
                                message.what = 1;
                                message.obj = sb.toString();
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = -1;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(baseurl+"like/"+uid);
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("PUT");
                            JSONObject params = new JSONObject();
                            params.put("pid",pid);
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
                                message.what = 1;
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
        togo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(baseurl+"todo/"+uid);
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("PUT");
                            JSONObject params = new JSONObject();
                            params.put("pid",pid);
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
                                message.what = 1;
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
    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case (android.R.id.home):
//                this.finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
