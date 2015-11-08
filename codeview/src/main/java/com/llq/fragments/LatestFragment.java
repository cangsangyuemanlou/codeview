package com.llq.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.llq.codeview.R;


public class LatestFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setText(getTag());
        return view;
    }

}
