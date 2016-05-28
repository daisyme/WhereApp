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
public class History extends Activity {
    ListView listView;
    String [] titles={"title1","title2","title3","title4"};
    String [] texts={"textA","textB","textC","textD"};

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
            LayoutInflater inflater = (LayoutInflater)History.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.listview_item, null);

            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(strTitle);
            TextView text = (TextView)itemView.findViewById(R.id.desc);
            text.setText(strText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(History.this,PoiInfo.class);
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
