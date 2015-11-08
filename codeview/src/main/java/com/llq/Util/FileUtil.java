package com.llq.Util;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llq on 2015/11/7.
 */
public class FileUtil {

    /**
     * 获取设备的所有存储设备
     *
     * @param context 上下文
     * @return Object[2] 元素为ArrayList类型 <br>
     * 第一个元素，保存存储设备列表是否可以被移除
     * false表示是不能被移除，即机身内存
     * true表示可以被移除，即扩展存储 <br>
     * 第二个元素，保存存储设备的路径 <br>
     */
    public static Object[] getStoragePath(Context context) {
        Object[] info = new Object[2];
        List<Boolean> bool = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = sm.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(sm);
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                bool.add(removable);
                paths.add(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        info[0] = bool;
        info[1] = paths;
        return info;
    }

    public static boolean isEmptyFileDir(String filePath) {
        File file = new File(filePath);
        return (file == null) || (file.listFiles() == null);
    }

    public static boolean isEmptyFileDir(File file){
        return  file.listFiles() == null;
    }

}
