package com.example.administrator.liliqing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.llq.views.MyCircleProgressBar;

public class ViewsActivity extends AppCompatActivity {

    private MyCircleProgressBar circleProgressBar;

    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);
        circleProgressBar = (MyCircleProgressBar) findViewById(R.id.myCircleProgressBar);
        max = circleProgressBar.getMax();
        new ProgressAnimation().execute();
    }

    class ProgressAnimation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= max; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            circleProgressBar.setProgress(values[0]);
            circleProgressBar.invalidate();
            super.onProgressUpdate(values);
        }
    }
}
