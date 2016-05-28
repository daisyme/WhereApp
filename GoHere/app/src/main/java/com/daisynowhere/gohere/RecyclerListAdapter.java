package com.daisynowhere.gohere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/24.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {
    private Context mContext;
    private String [] itemTiteles,itemTexts;

    public RecyclerListAdapter(Context mContext,String [] itemTitles, String [] itemTexts) {
        this.mContext = mContext;
        this.itemTexts = new String[itemTexts.length];
        this.itemTiteles = new String[itemTitles.length];
        this.itemTiteles=itemTitles;
        this.itemTexts=itemTexts;
    }

    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerListAdapter.ViewHolder holder, int position) {
        holder.Titel.setText(itemTiteles[position]);
        holder.Text.setText(itemTexts[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PoiInfo.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemTexts == null ? 0 : itemTexts.length;
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