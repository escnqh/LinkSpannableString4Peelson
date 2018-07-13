package com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;


import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.ClickableLinkSpan;
import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.OnClickStringCallback;
import com.meitu.qihangni.spantextproject.LinkSpannableString.VerticalImageSpan;

import com.meitu.qihangni.spantextproject.R;

import java.util.regex.Pattern;

/**
 * @author nqh 2018/7/12
 */
public class WebSpanMode implements BaseSpanMode {

    private static final String REGEX_WEB = "http://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    private static final String REGEX_WEB2 = "https://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    private static final String SCHEME_WEB2 = "https:";
    private static final String SCHEME_WEB = "https:";
    private int mTextSize = 20;
    private int mColor = R.color.colorPrimaryDark;
    private OnClickStringCallback mOnClickStringCallback;


    public WebSpanMode(int textSize, int color, @Nullable OnClickStringCallback onClickStringCallback) {
        this.mTextSize = textSize;
        this.mColor = color;
        this.mOnClickStringCallback = onClickStringCallback;
    }

    @Override
    public SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context) {
        Linkify.addLinks(spannableInfo, Pattern.compile(REGEX_WEB), SCHEME_WEB);
        Linkify.addLinks(spannableInfo, Pattern.compile(REGEX_WEB2), SCHEME_WEB2);
        URLSpan[] urlSpans = spannableInfo.getSpans(0, spannableInfo.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_WEB2) || urlSpan.getURL().startsWith(SCHEME_WEB)) {
                int start = spannableInfo.getSpanStart(urlSpan);
                int end = spannableInfo.getSpanEnd(urlSpan);
                spannableInfo.removeSpan(urlSpan);
                SpannableStringBuilder urlSpannableString = getUrlTextSpannableString(context, urlSpan.getURL());
                spannableInfo.replace(start, end, urlSpannableString);
                spannableInfo.setSpan(new ClickableLinkSpan(context, urlSpan.getURL(), mColor, mOnClickStringCallback), start, start + urlSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableInfo;
    }

    /**
     * @param context
     * @param source
     * @return 转换成网页链接形式的链接
     */
    private SpannableStringBuilder getUrlTextSpannableString(Context context, String source) {
        SpannableStringBuilder builder = new SpannableStringBuilder(source);
        String prefix = " ";
        builder.replace(0, prefix.length(), prefix);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_status_link);
        drawable.setBounds(0, 0, mTextSize, mTextSize);
        builder.setSpan(new VerticalImageSpan(drawable), prefix.length(), source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" 网页链接");
        // todo:  应当添加功能，可以将链接的标题返回作为文字提示
        return builder;
    }
}
