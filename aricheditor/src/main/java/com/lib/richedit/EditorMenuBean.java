package com.lib.richedit;

/**
 * -------------------------------------------
 * Dec:编辑器的按钮
 * Created by: Luojiusan on 2018/5/10--:9:17
 * Modify by:
 * -------------------------------------------
 **/
public class EditorMenuBean {
    public  boolean isSelectble=false;//是否选中状态
    public  boolean isClickble=true;//是否能够点击

    public EditorMenuBean(int menuType, int photoRes) {
        this.menuType = menuType;
        this.photoRes = photoRes;
    }

    public int menuType;//什么类型的按钮
    public int photoRes;//什么图标
}
