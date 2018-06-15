package com.meitu.qihangni.spantextproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * @author nqh 2018/6/14
 */
public class SomeTopic extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        textView=findViewById(R.id.tv_topic);
        Intent intent=getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            textView.setText(uri.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            textView.setText(uri.getHost());
        }
    }
}
