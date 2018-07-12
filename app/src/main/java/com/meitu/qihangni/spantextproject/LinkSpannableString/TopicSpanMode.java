package com.meitu.qihangni.spantextproject.LinkSpannableString;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import java.util.regex.Pattern;

/**
 * @author nqh 2018/7/12
 */
public class TopicSpanMode implements BaseSpanMode {

    public static final String REGEX_TOPIC = "#[^#]+#";
    public static final String SCHEME_TOPIC = "topic:";

    @Override
    public SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context, int color, @Nullable OnClickString onClickString) {
        Linkify.addLinks(spannableInfo, Pattern.compile(REGEX_TOPIC), SCHEME_TOPIC);
        URLSpan[] urlSpans = spannableInfo.getSpans(0, spannableInfo.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_TOPIC)) {
                int start = spannableInfo.getSpanStart(urlSpan);
                int end = spannableInfo.getSpanEnd(urlSpan);
                spannableInfo.removeSpan(urlSpan);
                spannableInfo.setSpan(new ClickableLinkSpan(context, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableInfo;
    }
}
