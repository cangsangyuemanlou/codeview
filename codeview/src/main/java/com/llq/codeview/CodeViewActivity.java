package com.llq.codeview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.llq.Util.FileUtil;
import com.llq.global.GlobalConfig;
import com.llq.holder.IconTreeItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

public class CodeViewActivity extends AppCompatActivity {

    public static final String SOURCE_FILE_PATH = "Source File Path";

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    // 点击屏幕后出现上下栏的延迟时间
    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            codeWebView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private WebView codeWebView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hello", "CodeViewActivity onCreate");
        setContentView(R.layout.activity_code_view);

        String sourceFilePath = getIntent().getStringExtra(
                CodeViewActivity.SOURCE_FILE_PATH);
        File sourceFile = new File(sourceFilePath);

        mVisible = true;

        initView();
        initWebView();

        showWebView(sourceFile);
    }

    private void showWebView(File sourceFile) {
        String encoding = FileUtil.getFileEncoding(sourceFile);
        String suffix = sourceFile.getAbsolutePath().substring(
                sourceFile.getAbsolutePath().lastIndexOf(".") + 1);
        String templateCode = loadTemplateCode();
        String sourceCode = loadSourceCode(sourceFile);
        String code = "";
        if (templateCode != null && sourceCode != null) {
            code = String.format(templateCode, encoding, suffix, sourceCode);
        }

        codeWebView.loadDataWithBaseURL("file:///android_asset/",
                code, "text/html", "utf-8", null);

        setTitle(sourceFile.getName());
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_code_view);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_code_view);
        mControlsView = findViewById(R.id.fullscreen_content_controls);

        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        ViewGroup treeContainer = (ViewGroup) findViewById(R.id.tree_view_left_side);
        if (!FileUtil.isEmptyFileDir(GlobalConfig.projectRootDir)) {
            TreeNode root = initTree();
            AndroidTreeView treeView = new AndroidTreeView(CodeViewActivity.this, root);
            treeView.setDefaultAnimation(true);
            treeView.setDefaultContainerStyle(R.style.TreeNodeStyle_Custom);
            treeView.setDefaultViewHolder(IconTreeItemHolder.class);
            treeContainer.addView(treeView.getView());
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        codeWebView = (WebView) findViewById(R.id.web_view_show_code);

        WebSettings webSettings = codeWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);

        codeWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        codeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        final GestureDetector detector = new GestureDetector(
                CodeViewActivity.this, new CodeViewGestureListener());

        codeWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    private String loadSourceCode(File sourceFile) {
        StringBuilder sBuilder = new StringBuilder();
        String encoding = FileUtil.getFileEncoding(sourceFile);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(sourceFile), encoding));
            String line;
            while ((line = br.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append("\n");
            }
            br.close();
            return sBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载模板代码
     * @return
     *  模板代码字符串，失败则返回null
     */
    private String loadTemplateCode() {
        try {
            StringBuilder sBuilder = new StringBuilder();
            InputStream is = getResources().getAssets().open("template/template.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append("\n");
            }
            br.close();
            return sBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TreeNode initTree() {
        TreeNode treeRoot = TreeNode.root();
        TreeNode projectRootDirNode = loadTree(GlobalConfig.projectRootDir);
        treeRoot.addChild(projectRootDirNode);
        return treeRoot;
    }

    private TreeNode loadTree(File dir) {
        TreeNode dirNode = new TreeNode(new IconTreeItemHolder.IconFileTreeItem(
                R.string.ic_folder, dir));
        dirNode.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                //将只有一个子文件夹的文件自动打开
                TreeNode tempNode = node;
                while (true) {
                    List<TreeNode> nodeChildren = tempNode.getChildren();
                    if (nodeChildren.size() == 1) {
                        TreeNode theOneNode = nodeChildren.get(0);
                        theOneNode.setExpanded(true);
                        tempNode = theOneNode;
                    } else {
                        break;
                    }
                }
            }
        });
        for (File file : FileUtil.loadFiles(dir)) {
            if (file.isDirectory()) {
                dirNode.addChild(loadTree(file));
            } else if (file.isFile() && isAllowedFile(file)) {
                TreeNode fileNode = new TreeNode(new IconTreeItemHolder.IconFileTreeItem(
                        R.string.ic_drive_file, file));
                fileNode.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        IconTreeItemHolder.IconFileTreeItem item = (IconTreeItemHolder.IconFileTreeItem) value;
                        showWebView(item.getFile());
                        drawerLayout.closeDrawers();
                    }
                });
                dirNode.addChild(fileNode);
            }
        }
        return dirNode;
    }

    private boolean isAllowedFile(File file) {
        Pattern pattern = Pattern.compile(GlobalConfig.allowedFileRegex,
                Pattern.CASE_INSENSITIVE);
        return pattern.matcher(file.getAbsolutePath()).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hello", "CodeViewActivity onDestroy");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(200);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        codeWebView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    // 手势监测
    class CodeViewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 单击
            toggle();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mVisible) {
                hide();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

}
