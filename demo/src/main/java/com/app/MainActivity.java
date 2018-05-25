package com.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.runtimepermissions.PermissionsManager;
import com.app.runtimepermissions.PermissionsResultAction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
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
