package com.daisynowhere.gohere;

/**
 * Created by Administrator on 2016/5/23.
 */
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowList extends Activity {
    ListView listView;
    String [] titles={"����1","����2","����3","����4"};
    String [] texts={"�ı�����A","�ı�����B","�ı�����C","�ı�����D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        this.setTitle("BaseAdapter for ListView");
        listView=(ListView)this.findViewById(R.id.MyListView);
        listView.setAdapter(new ListViewAdapter(titles,texts));
    }

    public class ListViewAdapter extends BaseAdapter{
        View [] itemViews;

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
            LayoutInflater inflater = (LayoutInflater)ShowList.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // ʹ��View�Ķ���itemView��R.layout.item����
            View itemView = inflater.inflate(R.layout.listview_item, null);

            // ͨ��findViewById()����ʵ��R.layout.item�ڸ����
            TextView title = (TextView)itemView.findViewById(R.id.title);
            title.setText(strTitle);
            TextView text = (TextView)itemView.findViewById(R.id.desc);
            text.setText(strText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ShowList.this,PoiInfo.class);
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

