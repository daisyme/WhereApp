package com.daisynowhere.gohere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {
    private Context mContext;
    private List<String> itemTiteles,itemTexts;
    private List<Integer> itemPids;
    private int uid;

    public RecyclerListAdapter(Context mContext,List<String> itemTitles, List<String> itemTexts, List<Integer> pids, int id) {
        this.mContext = mContext;
        this.itemTiteles=itemTitles;
        this.itemTexts=itemTexts;
        this.itemPids=pids;
        this.uid = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int p = position;
        holder.Titel.setText(itemTiteles.get(position));
        holder.Text.setText(itemTexts.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("PoiInfo");
                intent.putExtra("pid", itemPids.get(p));
                intent.putExtra("uid",uid);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemTexts == null ? 0 : itemTexts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView Text;
        public final TextView Titel;
        public final View view;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            Titel = (TextView) view.findViewById(R.id.title);
            Text = (TextView) view.findViewById(R.id.desc);
        }
    }
}