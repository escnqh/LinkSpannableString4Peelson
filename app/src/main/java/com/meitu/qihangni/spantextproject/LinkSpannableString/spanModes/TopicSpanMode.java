package com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.ClickableLinkSpan;
import com.meitu.qihangni.spantextproject.LinkSpannableString.clickCallbaks.OnClickStringCallback;
import com.meitu.qihangni.spantextproject.R;

import java.util.regex.Pattern;

/**
 * @author nqh 2018/7/12
 */
public class TopicSpanMode implements BaseSpanMode {

    public static final String REGEX_TOPIC = "#[^#]+#";
    public static final String SCHEME_TOPIC = "topic:";
    private int mColor = R.color.colorPrimaryDark;
    private OnClickStringCallback mOnClickStringCallback;

    public TopicSpanMode(int color, @Nullable OnClickStringCallback onClickStringCallback) {
        this.mColor = color;
        this.mOnClickStringCallback = onClickStringCallback;
    }

    @Override
    public SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context) {
        Linkify.addLinks(spannableInfo, Pattern.compile(REGEX_TOPIC), SCHEME_TOPIC);
        URLSpan[] urlSpans = spannableInfo.getSpans(0, spannableInfo.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_TOPIC)) {
                int start = spannableInfo.getSpanStart(urlSpan);
                int end = spannableInfo.getSpanEnd(urlSpan);
                spannableInfo.removeSpan(urlSpan);
                spannableInfo.setSpan(new ClickableLinkSpan(context, urlSpan.getURL(), mColor, mOnClickStringCallback), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableInfo;
    }
}
