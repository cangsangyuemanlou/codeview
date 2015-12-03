package com.llq.fragments;

import android.support.v4.app.Fragment;

import java.io.File;

public class BaseFragment extends Fragment {

    /**
     * 所有继承子类都在这个方法中处理Back键逻辑。 <br>
     * Activity捕捉到Back键，
     * 首先询问当前Fragment是否消费该事件，
     * 如果当前Fragment不消费，则Activity消费这个事件
     * @return
     * true：已经完整地处理了事件 <br>
     * false：没有完全处理，交给其他方法处理
     */
    public boolean onBackPressed(){
        return false;
    };

    public interface OnFileClickListener {
        void onFileClick(File file);
        void onFileLongClick(File file);
    }

    protected OnFileClickListener onFileClickListener;

}
