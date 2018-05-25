package com.lib.richedit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * -------------------------------------------
 * Dec:编辑器的底部按钮布局  富文本编辑器 简化代码只能传一张图一次
 * Created by: Luojiusan on 2018/5/10--:9:17
 * Modify by:
 * -------------------------------------------
 **/
public class ARichEditor implements EditorMenuTabBar.TabClickListener {
    EditorWebView mEditor;
    Context mContext;
    LinearLayout linear_bottom;
    LinearLayout linear_second_bottom;
    EditorMenuTabBar mMenuTabMainBar; //底部主要栏
    ArrayList<EditorMenuBean> mMainMenuData;
    EditorMenuTabBar mMenuTabSocondBar; //底部字体选择栏
    ArrayList<EditorMenuBean> mSecondMenuData;
    ProgressDialog progressDialog;

    public ARichEditor(Context mContext, RelativeLayout layoutParent) {
        this.mContext = mContext;
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.aricheditor, null);
        this.mEditor = rootview.findViewById(R.id.editor);
        linear_bottom = rootview.findViewById(R.id.linear_bottom);
        linear_second_bottom = rootview.findViewById(R.id.linear_second_bottom);
        this.linear_second_bottom.setVisibility(View.GONE);
        layoutParent.addView(rootview);
        initRichEditor();
        initData();

    }

    private void initData() {
        mMainMenuData = new ArrayList();
        EditorMenuBean menu1 = new EditorMenuBean(EditorMenuConfig.INSERT_IMAGE, R.drawable.editor_addphoto);
        EditorMenuBean menu2 = new EditorMenuBean(EditorMenuConfig.A, R.drawable.editor_a);
        EditorMenuBean menu3 = new EditorMenuBean(EditorMenuConfig.HALVING_LINE, R.drawable.editor_addline);
        EditorMenuBean menu4 = new EditorMenuBean(EditorMenuConfig.UNDO, R.drawable.editor_undo);
        EditorMenuBean menu5 = new EditorMenuBean(EditorMenuConfig.REDO, R.drawable.editor_redo);
        EditorMenuBean menu6 = new EditorMenuBean(EditorMenuConfig.SETTING, R.drawable.editor_setting);
        mMainMenuData.add(menu1);
        mMainMenuData.add(menu2);
        mMainMenuData.add(menu3);
        mMainMenuData.add(menu4);
        mMainMenuData.add(menu5);
        mMainMenuData.add(menu6);
        mMenuTabMainBar = new EditorMenuTabBar(mContext);
        mMenuTabMainBar.initViewData(mMainMenuData, linear_bottom);
        mMenuTabMainBar.setOnTabClickListener(this);

        mSecondMenuData = new ArrayList();
        EditorMenuBean sccondmenu1 = new EditorMenuBean(EditorMenuConfig.BOLD, R.drawable.editor_bold);
        EditorMenuBean sccondmenu2 = new EditorMenuBean(EditorMenuConfig.ITALIC, R.drawable.editor_italic);
        EditorMenuBean sccondmenu3 = new EditorMenuBean(EditorMenuConfig.STRIKE_THROUGH, R.drawable.editor_strike_through);
        EditorMenuBean sccondmenu4 = new EditorMenuBean(EditorMenuConfig.BLOCK_QUOTE, R.drawable.editor_block_quote);
        EditorMenuBean sccondmenu5 = new EditorMenuBean(EditorMenuConfig.H1, R.drawable.editor_h1);
        EditorMenuBean sccondmenu6 = new EditorMenuBean(EditorMenuConfig.LIST_CIRCLE, R.drawable.editor_listcircle);
        EditorMenuBean sccondmenu7 = new EditorMenuBean(EditorMenuConfig.LIST_NUMBER, R.drawable.editor_listnumber);
        mSecondMenuData.add(sccondmenu1);
        mSecondMenuData.add(sccondmenu2);
        mSecondMenuData.add(sccondmenu3);
        mSecondMenuData.add(sccondmenu4);
        mSecondMenuData.add(sccondmenu5);
        mSecondMenuData.add(sccondmenu6);
        mSecondMenuData.add(sccondmenu7);
        mMenuTabSocondBar = new EditorMenuTabBar(mContext);
        mMenuTabSocondBar.initViewData(mSecondMenuData, linear_second_bottom);
        mMenuTabSocondBar.setOnTabClickListener(this);
    }

    private void initRichEditor() {
        progressDialog = new ProgressDialog(mContext);//1.创建一个ProgressDialog的实例
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        //打开软键盘
        mEditor.focusEditor();
        mEditor.setOnTextChangeListener(new EditorWebView.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {

            }
        });
        /**
         * 拦截url 处理button 的选择状态
         */
        mEditor.setOnDecorationChangeListener(new EditorWebView.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<EditorWebView.Type> types) {
                for (EditorMenuBean temp : mSecondMenuData) {
                    if (temp.isSelectble) {
                        mMenuTabSocondBar.getTabView(temp.menuType).setSelected(false);
                    }
                    temp.isSelectble = false;
                }
                if (!TextUtils.isEmpty(text)) {
                    for (EditorMenuBean temp : mSecondMenuData) {
                        if (types.contains(EditorWebView.Type.bold) && EditorMenuConfig.BOLD == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.BOLD).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.h1) && EditorMenuConfig.H1 == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.H1).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.orderedlist) && EditorMenuConfig.LIST_NUMBER == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.LIST_NUMBER).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.italic) && EditorMenuConfig.ITALIC == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.ITALIC).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.strikeThrough) && EditorMenuConfig.STRIKE_THROUGH == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.STRIKE_THROUGH).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.unorderedList) && EditorMenuConfig.LIST_CIRCLE == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.LIST_CIRCLE).setSelected(true);
                            temp.isSelectble = true;
                        }
                        if (types.contains(EditorWebView.Type.blockquote) && EditorMenuConfig.BLOCK_QUOTE == temp.menuType) {
                            mMenuTabSocondBar.getTabView(EditorMenuConfig.BLOCK_QUOTE).setSelected(true);
                            temp.isSelectble = true;
                        }
                    }

                }
            }
        });
        /**
         * 自定义事件
         */
        mEditor.setRichEditorEventListner(new RichEditorEventListner() {
            @Override
            public void staticWords(long num) {

            }

            @Override
            public void titleKeyUpBack() {

            }

            @Override
            public void titlefocuse() {
                setButtonFocuseble(false);
            }

            @Override
            public void titleblur() {
                setButtonFocuseble(true);
            }

            @Override
            public void editorfocuse() {
                setButtonFocuseble(true);
            }

            @Override
            public void editorblur() {
                setButtonFocuseble(false);
            }
        });
    }

    /**
     * 是否能够点击
     *
     * @param focuseble
     */
    public void setButtonFocuseble(final boolean focuseble) {
        linear_bottom.post(new Runnable() {
            @Override
            public void run() {
                if (focuseble) {
                    linear_bottom.setAlpha(1);
                    linear_second_bottom.setAlpha(1);
                } else {
                    linear_bottom.setAlpha(0.5f);
                    linear_second_bottom.setAlpha(0.5f);
                }
                mMenuTabMainBar.setClickbale(focuseble);
                mMenuTabSocondBar.setClickbale(focuseble);
            }
        });
    }

    @Override
    public void onTabClickListener(ArrayList<ImageButton> listImageViewBar, View view, EditorMenuBean menu) {
        switch (menu.menuType) {
            case EditorMenuConfig.A:
                view.setSelected(menu.isSelectble);
                if (menu.isSelectble) {//如果被选择 展示处A选择框
                    linear_second_bottom.setVisibility(View.VISIBLE);
                } else {
                    linear_second_bottom.setVisibility(View.GONE);
                }
                break;
            case EditorMenuConfig.HALVING_LINE:
                mEditor.setSpiltLine();
                if (linear_bottom == null) {
                    return;
                }
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case EditorMenuConfig.UNDO:
                mEditor.undo();
                break;
            case EditorMenuConfig.REDO:
                mEditor.redo();
                break;
            case EditorMenuConfig.BOLD:
                mEditor.setBold();
                break;
            case EditorMenuConfig.ITALIC:
                mEditor.setItalic();
                break;
            case EditorMenuConfig.STRIKE_THROUGH:
                mEditor.setStrikeThrough();
                break;
            case EditorMenuConfig.BLOCK_QUOTE:
                mEditor.setBlockquote(menu.isSelectble);
                break;
            case EditorMenuConfig.LIST_CIRCLE:
                mEditor.setBulletsList();
                break;
            case EditorMenuConfig.LIST_NUMBER:
                mEditor.setNumbersList();
                break;
            case EditorMenuConfig.H1:
                mEditor.setHeading(1, menu.isSelectble);
                break;
            case EditorMenuConfig.SETTING:
                ToastMessage(mContext, "title:" + getTitle());
                ToastMessage(mContext, "content:" + getHtml());
                break;
            case EditorMenuConfig.INSERT_IMAGE:
                showcamrae((Activity) mContext, 1, new ArrayList<String>());
                break;
        }
    }

    /**
     * 弹出Toast消息
     */
    public static void ToastMessage(Context cont, String msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showcamrae(Activity context, int num, ArrayList<String> mResults) {
        PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(num)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)// 是否可预览图片 true or false
                .compressSavePath(getAppFilePath())//压缩图片保存地址
                .isCamera(true)// 是否显示拍照按钮 true or false
                .compress(true)// 是否压缩 true or false
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .isGif(true)// 是否显示gif图片 true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(REQUEST_IMAGE);//结果回调onActivityResult code
    }


    public String getHtml() {
        return mEditor.getHtml();
    }

    public String getTitle() {
        return mEditor.getTitle();
    }

    public long getContentLength() {
        return mEditor.getContentLength();
    }


    /**
     * 插入图片
     *
     * @param url
     * @param width
     * @param height
     */
    public void insertImage(String url, long width, long height) {
        mEditor.insertImage(url, width, height);
    }


    /**
     * 销毁编辑器避免webview泄露
     */
    protected void onDestroy() {
        if (mEditor != null) {
            mEditor.destroy();
        }
    }

    public static int REQUEST_IMAGE = 101;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMenuTabMainBar.getTabView(EditorMenuConfig.A).setSelected(false);
        linear_second_bottom.setVisibility(View.GONE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    // 获取返回的图片列表
                    List<String> imagePaths = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        imagePaths.add(selectList.get(i).getCompressPath()); //需要上传操作替换为网络地址
                    }
                    for (final String path : imagePaths) {
                        final long size[] = ImageUtils.getBitmapSize(path);
                        insertImage(path, size[0], size[1]);
                    }
                }
            }
        }
    }

    public static String getAppFilePath() {
        String filePath = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() + "/richedit" : "/mnt/sdcard/richedit";
        File appDir = new File(filePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File bstDir = new File(filePath, "/");
        if (!bstDir.exists()) {
            bstDir.mkdir();
        }
        return filePath + "/";
    }

}
