package com.llq.Util;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;

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

    // 加载文件夹dir下的所有文件
    public static List<File> loadFiles(File dir) {
        List<File> tempDir = new ArrayList<>();
        List<File> tempFile = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (!file.getName().startsWith(".")){
                // 过滤掉.xx文件
                if (file.isDirectory()) {
                    tempDir.add(file);
                } else if (file.isFile()) {
                    tempFile.add(file);
                }
            }
        }
        // 排序
        Comparator<File> comparator = new MyComparator();
        Collections.sort(tempDir, comparator);
        Collections.sort(tempFile, comparator);

        // 组合
        File[] tempFileArray = new File[tempDir.size() + tempFile.size()];
        System.arraycopy(tempDir.toArray(), 0, tempFileArray, 0, tempDir.size());
        System.arraycopy(tempFile.toArray(), 0, tempFileArray, tempDir.size(), tempFile.size());

        return Arrays.asList(tempFileArray);
    }

    public static class MyComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    }

    public static String getFileEncoding(File doc) {
        CodepageDetectorProxy detectorProxy = CodepageDetectorProxy.getInstance();
        detectorProxy.add(new ByteOrderMarkDetector());
        detectorProxy.add(new ParsingDetector(true));
        detectorProxy.add(JChardetFacade.getInstance());
        detectorProxy.add(ASCIIDetector.getInstance());

        try {
            Charset charset = detectorProxy.detectCodepage(doc.toURI().toURL());
            return charset.name();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "UTF-8";
    }

}
