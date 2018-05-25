package com.lib.richedit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * -------------------------------------------
 * Dec: 编辑器webview
 * Created by: Luojiusan on 2018/5/11--:14:48
 * Modify by:
 * -------------------------------------------
 **/
public class EditorWebView extends WebView {


    public enum Type {    //通过 监听editor click 事件 传达到 shouldOverrideUrlLoading 拦截掉对类型
        bold,
        italic,
        strikeThrough,
        underline,
        h1,
        orderedlist,
        unorderedList,
        blockquote,
    }

    public interface OnTextChangeListener {

        void onTextChange(String text);
    }

    public interface OnDecorationStateListener {

        void onStateChangeListener(String text, List<Type> types);
    }

    public interface AfterInitialLoadListener {

        void onAfterInitialLoad(boolean isReady);
    }

    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String STATE_SCHEME = "re-state://";  //用来拦截地址跳转实现 回调

    private String mContents;
    private long mContentLength;
    private String mTitle;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    RichEditorEventListner mRichEditorEventListner;
    public EditorWebView(Context context) {
        this(context, null);
    }

    public EditorWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint({"SetJavaScriptEnabled", "addJavascriptInterface"})
    public EditorWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode())
            return;

        addJavascriptInterface(new Android4JsInterface(), "AndroidInterface");
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(createWebviewClient());
        loadUrl(SETUP_HTML);

    }

    protected EditorWebViewClient createWebviewClient() {
        return new EditorWebViewClient();
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        mDecorationStateListener = listener;
    }



    /**
     * 检测编辑器点击事件 重置tabbar的图标
     *
     * @param text
     */
    private void stateCheck(String text) {
        String state = text.replaceFirst(STATE_SCHEME, "");
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.add(type);
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChangeListener(state, types);
        }
    }


    public void setHtml(String contents) {
        if (contents == null) {
            contents = "";
        }
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
        mContents = contents;
    }

    public String getHtml() {
        return mContents;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getContentLength() {
        return mContentLength;
    }

    public void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        Bitmap bitmap = ImageUtils.decodeResource(getContext(), resid);
        String base64 = ImageUtils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override
    public void setBackground(Drawable background) {
        Bitmap bitmap = ImageUtils.toBitmap(background);
        String base64 = ImageUtils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    public void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }


    public void setInputEnabled(Boolean inputEnabled) {
        exec("javascript:RE.setInputEnabled(" + inputEnabled + ")");
    }

    public void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public void undo() {
        exec("javascript:RE.undo();");
    }

    public void redo() {
        exec("javascript:RE.redo();");
    }

    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    /**
     * 添加分割线
     */
    public void setSpiltLine() {
        exec("javascript:RE.insertLine();");
    }

    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public void setFontSize(int fontSize) {
        if (fontSize > 7 || fontSize < 1) {
            Log.e("RichEditor", "Font size should have a value between 1-7");
        }
        exec("javascript:RE.setFontSize('" + fontSize + "');");
    }

    public void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    public void setHeading(int heading, boolean b) {
        if (b) {
            exec("javascript:RE.execformatBlock('h" + heading + "')");
        } else {
            exec("javascript:RE.execformatBlock('p')");
        }
    }

    public void setBlockquote(boolean b) {
        if (b) {
            exec("javascript:RE.execformatBlock('blockquote')");
        } else {
            exec("javascript:RE.execformatBlock('p')");
        }
    }

    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }


    public void setBulletsList() {
        exec("javascript:RE.setBullets();");
    }

    public void setNumbersList() {
        exec("javascript:RE.setNumbers();");
    }

    public void insertImage(String url, long width, long height) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', " + width + "," + height + ");");
    }

    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    public void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + ImageUtils.getCurrentTime() + "');");
    }

    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public void focusTitle() {
        requestFocus();
        exec("javascript:RE.title.focus();");
    }


    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public void exec(final String trigger) {
        loadUrl(trigger);
    }


    protected class EditorWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // No handling
                return false;
            }
            if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * 自定义事件
     *
     * @param mRichEditorEventListner
     */
    public void setRichEditorEventListner(RichEditorEventListner mRichEditorEventListner) {
        this.mRichEditorEventListner = mRichEditorEventListner;
    }

    private class Android4JsInterface {

        @JavascriptInterface
        public void staticWords(long num) {
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.staticWords(num);
            }
        }

        @JavascriptInterface
        public void titleKeyUpBack() { //title 回车键
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.titleKeyUpBack();
            }
        }

        @JavascriptInterface   //标题得到焦点
        public void titlefocuse() {
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.titlefocuse();
            }
        }

        @JavascriptInterface//标题失去焦点
        public void titleblur() {
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.titleblur();
            }
        }

        @JavascriptInterface//内容得到焦点
        public void editorfocuse() {
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.editorfocuse();
            }
        }

        @JavascriptInterface//内容失去焦点
        public void editorblur() {
            if (mRichEditorEventListner != null) {
                mRichEditorEventListner.editorblur();
            }
        }

        @JavascriptInterface//内容刷新
        public void textChange(String text) {
            mContents = text;
            if (mTextChangeListener != null) {
                mTextChangeListener.onTextChange(mContents);
            }
        }

        @JavascriptInterface//标题刷新
        public void titleChange(String text) {
            mTitle = text;
        }

        @JavascriptInterface//标题刷新
        public void contentLength(long length) {
            mContentLength = length;
        }
    }
}
