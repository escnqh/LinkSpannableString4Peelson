package com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.meitu.qihangni.spantextproject.LinkSpannableString.LinkSpanTool;
import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.OnClickStringCallback;
import com.meitu.qihangni.spantextproject.R;

import java.util.List;

/**
 * 截获富文本的点击事件
 *
 * @author nqh 2018/6/15
 */
public class ClickableLinkSpan extends ClickableSpan implements View.OnClickListener {
    private Context mContext;
    private String string;
    public int textcolor = 0xffeeeeee;
    private boolean mIsPressed;
    private OnClickStringCallback mOnClickStringCallback;

    public ClickableLinkSpan(Context context, String string, int color, @Nullable OnClickStringCallback mOnClickStringCallback) {
        this.mContext = context;
        this.string = string;
        this.mOnClickStringCallback = mOnClickStringCallback;

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
        //判断用户是否自定义点击事件
        if (null != mOnClickStringCallback) {
            mOnClickStringCallback.onClickString(string);
        } else {
            doAction(string);
        }
    }

    /**
     * 对点击焦点的富文本做出反应
     *
     * @param string
     */
    private void doAction(String string) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(string));
        //判断目标是否可达
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isValid = !activities.isEmpty();
        if (isValid) {
            mContext.startActivity(intent);
        }
    }

    /**
     * 设置文本的样式
     *
     * @param tp
     */
    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(mIsPressed ? mContext.getResources().getColor(android.R.color.black) : textcolor);
        tp.bgColor = mIsPressed ? 0xffeeeeee : mContext.getResources().getColor(android.R.color.transparent);
        //设置取消下划线
        tp.setUnderlineText(false);
        tp.clearShadowLayer();
    }
}
