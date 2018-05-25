package com.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.app.runtimepermissions.PermissionsManager;
import com.app.runtimepermissions.PermissionsResultAction;
import com.lib.richedit.ARichEditor;

public class MainActivity extends AppCompatActivity {
    ARichEditor mRichEditor;
    RelativeLayout rl_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();
    }

    private void initView() {
        rl_layout = findViewById(R.id.rl_layout);
        mRichEditor = new ARichEditor(this, rl_layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRichEditor.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * android6.0动态权限申请
     */
    private void requestPermission() {

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }
}
