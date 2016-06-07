package com.daisynowhere.gohere;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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
    List<String > titles,texts,pids;
    RecyclerView recyclerView;
    private int id;
    private String lat,lon;
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
                        String text = jsonObject.getString("address");
                        String pid = jsonObject.getString("pid");
                        titles.add(title);
                        texts.add(text);
                        pids.add(pid);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                    recyclerView.setAdapter(new RecyclerListAdapter(getActivity(), titles, texts, pids, id));
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
        pids = new ArrayList<String>();
//        Location location = getLocation(this.getActivity());
//        lat = ""+location.getLatitude();
//        lon = ""+location.getLongitude();
        lat = "30.27";
        lon = "120.16";

//        titles.add("1");
//        titles.add("2");
//        texts.add("1");
//        texts.add("2");
//        pids.add("1");
//        pids.add("2");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"result/"+id);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    JSONObject params = new JSONObject();
                    params.put("lat",lat);
                    params.put("lon",lon);
                    params.put("radius",3);
                    Log.i("TAG","Test");
                    httpURLConnection.connect();
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(params.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
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
        thread.start();
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

    private Location getLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location == null){
            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        }
        return location;
    }

}