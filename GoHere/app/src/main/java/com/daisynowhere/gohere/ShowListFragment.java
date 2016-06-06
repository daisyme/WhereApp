package com.daisynowhere.gohere;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ShowListFragment extends Fragment {
    ListView listView;
    //String [] titles={"����1","����2","����3","����4"};
    //String [] texts={"�ı�����A","�ı�����B","�ı�����C","�ı�����D"};
    List<String > titles,texts;
    List<Integer> pids;
    RecyclerView recyclerView;
    private int id;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                try{
                    JSONArray jsonArray = new JSONArray((String)msg.obj);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("name");
                        String text = jsonObject.getString("desc");
                        int pid = jsonObject.getInt("pid");
                        titles.add(title);
                        texts.add(text);
                        pids.add(pid);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        id = ((MainActivity)getActivity()).getId();
        recyclerView = (RecyclerView) inflater.inflate(R.layout.info_details_fragment,container,false);
        titles = new ArrayList<String>();
        texts = new ArrayList<String>();
        pids = new ArrayList<Integer>();

        titles.add("1");
        titles.add("2");
        texts.add("1");
        texts.add("2");
        pids.add(1);
        pids.add(2);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"POI");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    int resultcode = httpURLConnection.getResponseCode();
                    if (resultcode == HttpURLConnection.HTTP_OK) {
                        InputStream is = httpURLConnection.getInputStream();

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
                        Log.i("TAG","111111111111");
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
        });
//        thread.start();
//        try{
//            thread.join();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return recyclerView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerListAdapter(getActivity(), titles, texts, pids, id));
    }

}