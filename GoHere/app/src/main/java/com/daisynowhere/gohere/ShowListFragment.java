package com.daisynowhere.gohere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ShowListFragment extends Fragment {
    ListView listView;
    String [] titles={"����1","����2","����3","����4"};
    String [] texts={"�ı�����A","�ı�����B","�ı�����C","�ı�����D"};
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        recyclerView = (RecyclerView) inflater.inflate(R.layout.info_details_fragment,container,false);
        return recyclerView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerListAdapter(getActivity(),titles,texts));
    }

}