package com.llq.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.administrator.liliqing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    private View parentView;
    private TextInputLayout textInputLayout;
    private FloatingActionButton floatingActionButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_agenda, container, false);
        return parentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textTextInputLayout();

        textFloatingActionButton();
    }


    private void textTextInputLayout() {

        textInputLayout = (TextInputLayout) parentView.findViewById(R.id.text_input_layout);
        final EditText editText = textInputLayout.getEditText();
        textInputLayout.setHint("请输入4位学号");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("llq", "beforeTextChanged" + s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("llq", "onTextChanged" + s);
                if (s.length() > 4) {
                    textInputLayout.setError("学号输入错误！");
                    textInputLayout.setErrorEnabled(true);
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("llq", "afterTextChanged" + s);
            }
        });
    }

    private void textFloatingActionButton() {

        floatingActionButton = (FloatingActionButton) parentView.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "结束当前Activity", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });
    }

}
