package com.meitu.qihangni.spantextproject.LinkSpannableString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.TextView;

import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.OnClickStringCallback;
import com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes.BaseSpanMode;

import java.lang.ref.WeakReference;

/**
 * @author nqh 2018/6/14
 */
public class LinkSpanTool {

    private static WeakReference<Context> mContextWeakReference;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static SpannableStringBuilder mSpannableInfo;
    private static SpanModeManager mSpanModeManager;


    public static void linkSpan(Spanned content, TextView textView, SpanModeManager spanModeManager) {
        mSpanModeManager = spanModeManager;
        textView.setText(getSpan(textView.getContext(), content));
        textView.setMovementMethod(new LinkTouchMovementMethod());
    }

    /**
     * @param context
     * @param spanned
     * @return 返回处理过的文本
     */
    public static SpannableStringBuilder getSpan(Context context, Spanned spanned) {
        mContextWeakReference = new WeakReference<>(context);
        mSpannableInfo = new SpannableStringBuilder(spanned);
        mContext = mContextWeakReference.get();
        for (BaseSpanMode spanMode : mSpanModeManager.getSpanModeList()) {
            mSpannableInfo = spanMode.linkSpan(mSpannableInfo, mContext);
        }
        return mSpannableInfo;
    }

}
