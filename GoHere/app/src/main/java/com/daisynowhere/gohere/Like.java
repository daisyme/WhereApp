package com.daisynowhere.gohere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Like extends Activity {
    ListView listView;
    String [] titles={"标题1","标题2","标题3","标题4"};
    String [] texts={"文本内容A","文本内容B","文本内容C","文本内容D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        this.setTitle("BaseAdapter for ListView");
        listView=(ListView)this.findViewById(R.id.MyListView);
        listView.setAdapter(new ListViewAdapter(titles,texts));
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(String [] itemTitles, String [] itemTexts){
            itemViews = new View[itemTitles.length];

            for (int i=0; i<itemViews.length; ++i){
                itemViews[i] = makeItemView(itemTitles[i], itemTexts[i]);
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

        private View makeItemView(String strTitle, String strText) {
            LayoutInflater inflater = (LayoutInflater)Like.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.listview_item, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(strTitle);
            TextView text = (TextView)itemView.findViewById(R.id.desc);
            text.setText(strText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(Like.this,PoiInfo.class);
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
