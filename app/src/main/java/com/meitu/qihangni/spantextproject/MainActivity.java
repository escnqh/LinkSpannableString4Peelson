package com.meitu.qihangni.spantextproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.meitu.qihangni.spantextproject.LinkSpannableString.LinkSpanTool;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_str);
        LinkSpanTool.Config config = new LinkSpanTool.Config()
                .at(false);
        SpannableString spannableString = new SpannableString("#topic# @userxxx,blog is https://www.baidu.com #another# #123#");
        LinkSpanTool.linkSpan(spannableString, textView, R.color.colorPrimaryDark, null, null);
    }
}
