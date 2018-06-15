package com.meitu.qihangni.spantextproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

/**
 * @author nqh 2018/6/14
 */
public class LinkSpanTool {

    private static WeakReference<Context> contextWeakReference;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static SpannableStringBuilder spanableInfo;
    private static SpannableString spanableInfo_temp;
    private static boolean mWeb = true;
    private static boolean mAt = true;
    private static boolean mTopic = true;
    private static boolean mPage = true;
    private static boolean isCustomClick = false;
    private static float mTextSize;

    private static class LinkPattern {
        public static final String REGEX_TOPIC = "#[^#]+#";
        public static final String REGEX_WEB = "http://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
        public static final String REGEX_WEB2 = "https://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
        public static final String REGEXT_AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";

        public static final String SCHEME_TOPIC = "topic:";
        public static final String SCHEME_URL = "url:";
        public static final String SCHEME_AT = "at:";
    }

    public static void linkSpan(Spanned content, TextView textView, int color, @Nullable OnClickString onClickString, @Nullable Config config) {
        mTextSize = textView.getTextSize();
        if (null != onClickString) {
            isCustomClick = true;
        }
        if (null != config) {
            configure(config);
        }
        textView.setText(getSpan(textView.getContext(), content, color, onClickString));
        textView.setMovementMethod(new LinkTouchMovementMethod());

    }

    public static boolean isCustomClick() {
        return isCustomClick;
    }

    public static SpannableStringBuilder getSpan(Context context, Spanned spanned, int color, @Nullable OnClickString onClickString) {
        contextWeakReference = new WeakReference<>(context);
        spanableInfo = new SpannableStringBuilder(spanned);
        spanableInfo_temp = new SpannableString(spanned);
        mContext = contextWeakReference.get();
        initLink();
        URLSpan[] urlSpans = spanableInfo_temp.getSpans(0, spanableInfo_temp.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_URL)) {
                int start = spanableInfo_temp.getSpanStart(urlSpan);
                int end = spanableInfo_temp.getSpanEnd(urlSpan);
                spanableInfo_temp.removeSpan(urlSpan);
                SpannableStringBuilder urlSpannableString = getUrlTextSpannableString(context, urlSpan.getURL());
                spanableInfo.replace(start, end, urlSpannableString);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, start + urlSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_TOPIC)) {
                int start = spanableInfo_temp.getSpanStart(urlSpan);
                int end = spanableInfo_temp.getSpanEnd(urlSpan);
                spanableInfo_temp.removeSpan(urlSpan);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_AT)) {
                int start = spanableInfo_temp.getSpanStart(urlSpan);
                int end = spanableInfo_temp.getSpanEnd(urlSpan);
                spanableInfo_temp.removeSpan(urlSpan);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return spanableInfo;
    }


    private static void initLink() {
        if (mWeb) {
            initWeb();
        }
        if (mAt) {
            initAt();
        }
        if (mTopic) {
            initTopic();
        }
        if (mPage) {
            initPage();
        }
    }

    /**
     * 识别内部页面
     */
    private static void initPage() {

    }

    /**
     * 识别话题
     */
    private static void initTopic() {
        Linkify.addLinks(spanableInfo_temp, Pattern.compile(LinkPattern.REGEX_TOPIC), LinkPattern.SCHEME_TOPIC);
    }

    /**
     * 识别网址
     */
    private static void initWeb() {
        Linkify.addLinks(spanableInfo_temp, Pattern.compile(LinkPattern.REGEX_WEB), LinkPattern.SCHEME_URL);
        Linkify.addLinks(spanableInfo_temp, Pattern.compile(LinkPattern.REGEX_WEB2), LinkPattern.SCHEME_URL);
    }

    /**
     * 识别@
     */
    private static void initAt() {
        Linkify.addLinks(spanableInfo_temp, Pattern.compile(LinkPattern.REGEXT_AT), LinkPattern.SCHEME_AT);
    }

    interface OnClickString {
        void onClickString(String string);
    }

    /**
     * 配置支持哪些链接
     */
    public static class Config {
        private boolean web = true;
        private boolean at = true;
        private boolean topic = true;
        private boolean page = true;

        public Config web(boolean isTrue) {
            this.web = isTrue;
            return this;
        }

        public Config at(boolean isTrue) {
            this.at = isTrue;
            return this;
        }

        public Config topic(boolean isTrue) {
            this.topic = isTrue;
            return this;
        }

        public Config page(boolean isTrue) {
            this.page = isTrue;
            return this;
        }

        protected void deal() {
            mWeb = web;
            mAt = at;
            mTopic = topic;
            mPage = page;
        }
    }

    /**
     * 生效配置
     *
     * @param config 配置
     */
    public static void configure(Config config) {
        config.deal();
    }

    private static SpannableStringBuilder getUrlTextSpannableString(Context context, String source) {
        SpannableStringBuilder builder = new SpannableStringBuilder(source);
        String prefix = " ";
        builder.replace(0, prefix.length(), prefix);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_status_link);
        drawable.setBounds(0, 0, (int) mTextSize, (int) mTextSize);
        builder.setSpan(new VerticalImageSpan(drawable), prefix.length(), source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" 网页链接");
        return builder;
    }

    private String getWebTitle(String url) {
        String title = "";
        return title;
    }

}
