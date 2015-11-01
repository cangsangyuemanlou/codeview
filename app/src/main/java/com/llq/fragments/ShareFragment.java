package com.llq.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.liliqing.R;
import com.example.administrator.liliqing.RecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {

    private View parentView;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_share, container, false);
        return parentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                recyclerView.getContext()
        );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity()));
    }
}
