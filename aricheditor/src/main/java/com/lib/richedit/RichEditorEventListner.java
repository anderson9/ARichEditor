package com.lib.richedit;

/**
 * -------------------------------------------
 * Dec: 编辑器 事件回调
 * Created by: Luojiusan on 2018/5/11--:14:48
 * Modify by:
 * -------------------------------------------
 **/
public interface RichEditorEventListner {
    public void staticWords(long num) ;
    public void titleKeyUpBack();
    public void titlefocuse() ; //标题焦点
    public void titleblur() ;
    public void editorfocuse() ;//内容焦点
    public void editorblur() ;
}
