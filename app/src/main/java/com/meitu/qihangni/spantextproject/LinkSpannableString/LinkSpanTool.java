package com.meitu.qihangni.spantextproject.LinkSpannableString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author nqh 2018/6/14
 */
public class LinkSpanTool {

    private static WeakReference<Context> mContextWeakReference;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static SpannableStringBuilder mSpanableInfo;
    private static boolean isCustomClick = false;
    private static float mTextSize;
    private static SpanModeManager mSpanModeManager;


    public static void linkSpan(Spanned content, TextView textView, SpanModeManager spanModeManager, int color, @Nullable OnClickString onClickString) {
        mTextSize = textView.getTextSize();
        mSpanModeManager = spanModeManager;
        if (null != onClickString) {
            isCustomClick = true;
        }
        textView.setText(getSpan(textView.getContext(), content, color, onClickString));
        textView.setMovementMethod(new LinkTouchMovementMethod());
    }

    /**
     * @return 是否是用户自定义点击事件
     */
    public static boolean isCustomClick() {
        return isCustomClick;
    }

    /**
     * @param context
     * @param spanned
     * @param color
     * @param onClickString
     * @return 返回处理过的文本
     */
    public static SpannableStringBuilder getSpan(Context context, Spanned spanned, int color, @Nullable OnClickString onClickString) {
        mContextWeakReference = new WeakReference<>(context);
        mSpanableInfo = new SpannableStringBuilder(spanned);
        mContext = mContextWeakReference.get();
        for (BaseSpanMode spanMode : mSpanModeManager.getSpanModeList()) {
            mSpanableInfo = spanMode.linkSpan(mSpanableInfo, mContext, color, onClickString);
        }
        return mSpanableInfo;
    }

}
