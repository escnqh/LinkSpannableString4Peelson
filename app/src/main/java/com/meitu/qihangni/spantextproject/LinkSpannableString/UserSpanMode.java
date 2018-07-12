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
public class UserSpanMode implements BaseSpanMode {
    private static final String REGEXT_AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";
    private static final String SCHEME_AT = "user:";

    @Override
    public SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context, int color, @Nullable OnClickString onClickString) {
        Linkify.addLinks(spannableInfo, Pattern.compile(REGEXT_AT), SCHEME_AT);
        URLSpan[] urlSpans = spannableInfo.getSpans(0, spannableInfo.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_AT)) {
                int start = spannableInfo.getSpanStart(urlSpan);
                int end = spannableInfo.getSpanEnd(urlSpan);
                spannableInfo.removeSpan(urlSpan);
                spannableInfo.setSpan(new ClickableLinkSpan(context, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableInfo;
    }
}
