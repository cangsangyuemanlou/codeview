package com.llq.global;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/* xml存放数据
名称：codeview
位置：/data/data/com.llq.codeview/shared_prefs/codeview.xml
内容：
<map>

<root_files_count>2</root_files_count>
<root_files_0>000</root_files_0>
<root_files_1>111</root_files_1>

<latest_queue_count>10</latest_queue_count>
<latest_queue_0>000</latest_queue_0>
<latest_queue_1>111</latest_queue_1>
...
<latest_queue_9>999</latest_queue_9>

<project_root_dir>project</project_root_dir>

</map>
 */
public class SharedPreferencesHelper {

    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("codeview", Context.MODE_PRIVATE);
    }

    public void initGlobalConfig() {
        // restore rootFiles
        GlobalConfig.rootFiles.clear();
        int count = sharedPreferences.getInt("root_files_count", 0);
        for (int i = 0; i < count; i++) {
            String key = "root_files_" + i;
            String path = sharedPreferences.getString(key, "");
            GlobalConfig.rootFiles.add(new File(path));
        }
        // restore latestQueue
        GlobalConfig.latestQueue.clear();
        count = sharedPreferences.getInt("latest_queue_count", 0);
        for (int i = count - 1; i >= 0; i--) {
            String key = "latest_queue_" + i;
            String path = sharedPreferences.getString(key, "");
            GlobalConfig.latestQueue.addItem(new File(path));
        }
        // restore projectRootDir
        String path = sharedPreferences.getString("project_root_dir", "");
        GlobalConfig.projectRootDir = new File(path);
    }

    public void restoreGlobalConfig() {
        initGlobalConfig();
    }

    public void saveGlobalConfig() {
        editor = sharedPreferences.edit();
        editor.clear();
        int size = GlobalConfig.rootFiles.size();
        editor.putInt("root_files_count", size);
        for (int i = 0; i < size; i++) {
            String key = "root_files_" + i;
            editor.putString(key, GlobalConfig.rootFiles.get(i).getAbsolutePath());
        }
        size = GlobalConfig.latestQueue.size();
        editor.putInt("latest_queue_count", size);
        for (int i = 0; i < size; i++) {
            String key = "latest_queue_" + i;
            editor.putString(key, GlobalConfig.latestQueue.toList().get(i).getAbsolutePath());
        }
        editor.putString("project_root_dir", GlobalConfig.projectRootDir.getAbsolutePath());
        editor.apply();
    }

}
