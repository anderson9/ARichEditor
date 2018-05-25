package com.lib.richedit;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * -------------------------------------------
 * Dec:编辑器的底部按钮基础组件
 * Created by: Luojiusan on 2018/5/10--:9:17
 * Modify by:
 * -------------------------------------------
 **/
public class EditorMenuTabBar extends View {
    /* 按钮对象集合 */
    private ArrayList<EditorMenuBean> mDatas;
    /* 存放bar的框架 */
    private LinearLayout mLinearLayout;
    /* 上下文环境 */
    private Context mContext;

    public void setClickbale(boolean clickbale) {
        isClickbale = clickbale;
    }

    private boolean isClickbale = true;//整个tabbar是否有点击事件

    /* 存放组件 */
    private ArrayList<ImageButton> listImageViewBar = new ArrayList<ImageButton>();
    private HashMap<Integer, View> mViewTab = new HashMap<>();

    private int lineCount;//一屏幕默认几个按钮  默认是mdata的size

    public EditorMenuTabBar(Context context) {
        super(context);
        this.mContext = context;
    }

    public EditorMenuTabBar(Context context, int lineCount) {
        super(context);
        this.mContext = context;
        this.lineCount = lineCount;
    }

    /*
     * 初始化
     */
    public void initViewData(ArrayList mDatas, LinearLayout layoutTabBar) {
        this.mDatas = mDatas;
        mViewTab = new HashMap<>();
        this.mLinearLayout = layoutTabBar;
        lineCount = mDatas.size();
        // 初始化组件
        initView();
    }


    private void initView() {
        LinearLayout tabContainer = new LinearLayout(mContext);
        tabContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabContainer.setGravity(Gravity.CENTER_VERTICAL);
        int screenWidth = getScreenWidth(mContext);
        for (int i = 0; i < lineCount; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.medule_view_editor_item, null);
            ImageButton imagebutton = view.findViewById(R.id.imagebutton);
            imagebutton.setImageResource(mDatas.get(i).photoRes);
            tabContainer.addView(view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
            params.width = screenWidth / lineCount;
            params.height = params.width;
            int padinng = params.width / lineCount;
            imagebutton.setPadding(padinng, padinng, padinng, padinng);//
            view.setLayoutParams(params);
            mViewTab.put(mDatas.get(i).menuType, imagebutton);
            listImageViewBar.add(imagebutton);
        }
        mLinearLayout.addView(tabContainer);
    }

    /**
     * 得到指定的View
     *
     * @param menuType
     * @return
     */
    public View getTabView(int menuType) {
        return mViewTab.get(menuType);
    }


    public interface TabClickListener {
        void onTabClickListener(ArrayList<ImageButton> listImageViewBar, View view, EditorMenuBean menu);
    }

    public void setOnTabClickListener(final TabClickListener myTabClickListener) {
        if (listImageViewBar != null) {

            for (int i = 0; i < listImageViewBar.size(); i++) {
                final int finalI = i;
                listImageViewBar.get(i).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 返回选中的tabIndex
                        if (isClickbale) {
                            EditorMenuBean editorMenu = mDatas.get(finalI);
                            view.setClickable(editorMenu.isClickble);  //设置是否可点击状态

                            editorMenu.isSelectble = !editorMenu.isSelectble;
                            view.setSelected(editorMenu.isSelectble);//选择的状态转变  字体选择
                            myTabClickListener.onTabClickListener(listImageViewBar, view, mDatas.get(finalI));
                        }
                    }
                });
            }

        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }
}
