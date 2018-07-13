package com.meitu.qihangni.spantextproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.meitu.qihangni.spantextproject.LinkSpannableString.LinkSpanTool;
import com.meitu.qihangni.spantextproject.LinkSpannableString.SpanModeManager;
import com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes.TopicSpanMode;
import com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes.UserSpanMode;
import com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes.WebSpanMode;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_content = findViewById(R.id.tv_str);
        SpannableString spannableString = new SpannableString("#topic# @userxxx,blog is https://www.baidu.com #another# #123#");
        SpanModeManager spanModeManager = new SpanModeManager()
                .addMode(new TopicSpanMode(R.color.colorAccent, null))
                .addMode(new UserSpanMode(R.color.colorPrimaryDark, null))
                .addMode(new WebSpanMode((int) tv_content.getTextSize(), R.color.colorPrimaryDark, null))
                .linkSpan(spannableString,tv_content);

    }
}
