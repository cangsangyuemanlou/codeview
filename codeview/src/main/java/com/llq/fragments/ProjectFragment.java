package com.llq.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.llq.codeview.R;


public class ProjectFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        TextView tv = (TextView) view.findViewById(R.id.project_text_view);
        tv.setText(getTag());
        return view;
    }

}
