package com.llq.global;

import java.io.File;
import java.util.ArrayList;

/**
 * 程序相关配置集合，部分设置存储到SharePreference
 */
public class GlobalConfig {

    // 手机存储根目录
    // 如果手机有多个存储设备
    // 第一个元素为内部存储，后面的为扩展存储
    public static ArrayList<File> rootFiles = new ArrayList<>();

    // 最近使用的文件
    public static LatestQueue<File> latestQueue = new LatestQueue<>(10);

    public static String[] allowedFileSuffix = {"java", "html", "txt", "xml"};

    public static String allowedFileRegex = ".*\\.java|.*\\.html|.*\\.txt|.*\\.xml";

    public static File projectRootDir;

}
