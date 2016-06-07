package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Like extends Activity {
    ListView listView;
    String [] titles={"����1","����2","����3","����4"};
    String [] texts={"�ı�����A","�ı�����B","�ı�����C","�ı�����D"};
    String [] pids;
    private int id;
    String baseurl = "http://10.0.2.2:8000/";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                try{
                    JSONArray jsonArray = new JSONArray((String)msg.obj);
                    titles = new String[jsonArray.length()];
                    texts = new String[jsonArray.length()];
                    pids = new String[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("name");
                        String text = jsonObject.getString("address");
                        String pid = jsonObject.getString("pid");
                        titles[i] = title;
                        texts[i] = text;
                        pids[i] = pid;
                    }
                    listView.setAdapter(new ListViewAdapter(titles,texts,pids));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        this.setTitle("BaseAdapter for ListView");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl+"like/"+id);
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
        });
        thread.start();
//        try{
//            thread.join();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        listView=(ListView)this.findViewById(R.id.MyListView);
        //listView.setAdapter(new ListViewAdapter(titles,texts,pids));
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(String [] itemTitles, String [] itemTexts, String [] itempids){
            itemViews = new View[itemTitles.length];

            for (int i=0; i<itemViews.length; ++i){
                itemViews[i] = makeItemView(itemTitles[i], itemTexts[i], itempids[i]);
            }
        }

        public int getCount()   {
            return itemViews.length;
        }

        public View getItem(int position)   {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }

        private View makeItemView(String strTitle, String strText, String pid) {
            LayoutInflater inflater = (LayoutInflater)Like.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // ʹ��View�Ķ���itemView��R.layout.item����
            View itemView = inflater.inflate(R.layout.listview_item, null);

            // ͨ��findViewById()����ʵ��R.layout.item�ڸ����
            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(strTitle);
            TextView text = (TextView)itemView.findViewById(R.id.desc);
            text.setText(strText);
            final String p = pid;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("PoiInfo");
                    intent.putExtra("pid",p);
                    intent.putExtra("uid", id);
                    startActivity(intent);
                }
            });

            return itemView;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }
}
