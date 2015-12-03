package com.llq.Util;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

public class DataStorageHelper {

    private Context mContext;

    public DataStorageHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void saveFiles(String savedFileName, Iterable<File> files) {
        try {
            FileOutputStream fos = mContext.openFileOutput(savedFileName, Context.MODE_PRIVATE);
            Iterator<File> it = files.iterator();
            while (it.hasNext()) {
                Log.i("hello", it.next().toString());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void loadFiles() {

    }

}
