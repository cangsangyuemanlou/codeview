package com.llq.Util;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Iterator;

public class DataStorageHelper {

    private Context mContext;

    public DataStorageHelper(Context mContext) {

    }

    public static void saveFiles(Iterable<File> files) {
        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            Log.i("hello", it.next().toString());
        }
    }

    public static void loadFiles() {

    }

}
