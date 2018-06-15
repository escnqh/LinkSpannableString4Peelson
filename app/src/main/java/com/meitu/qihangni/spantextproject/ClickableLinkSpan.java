package com.meitu.qihangni.spantextproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @author nqh 2018/6/15
 */
class ClickableLinkSpan extends ClickableSpan implements View.OnClickListener {
    private Context mContext;
    private String string;
    public int textcolor = 0xffeeeeee;
    private boolean mIsPressed;
    private LinkSpanTool.OnClickString mOnClickString;

    public ClickableLinkSpan(Context context, String string, int color, @Nullable LinkSpanTool.OnClickString mOnClickString) {
        this.mContext = context;
        this.string = string;
        this.mOnClickString = mOnClickString;

        //设置颜色
        if (color != -100) {
            textcolor = this.mContext.getResources().getColor(color);
        } else {
            textcolor = this.mContext.getResources().getColor(R.color.colorPrimary);
        }
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    @Override
    public void onClick(View v) {
        v.setTag("false");
        Log.i("111111111", string);
        if (LinkSpanTool.isCustomClick()) {
            mOnClickString.onClickString(string);
        } else {
            doAction(string);
        }
    }

    private void doAction(String string) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(string));
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isValid = !activities.isEmpty();
        if (isValid) {
            mContext.startActivity(intent);
        }
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        // TODO Auto-generated method stub
        tp.setColor(mIsPressed ? mContext.getResources().getColor(android.R.color.black) : textcolor);
        tp.bgColor = mIsPressed ? 0xffeeeeee : mContext.getResources().getColor(android.R.color.transparent);
        tp.setUnderlineText(false);//设置下划线
        tp.clearShadowLayer();
    }
}
