package com.meitu.qihangni.spantextproject.LinkSpannableString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;
import com.meitu.qihangni.spantextproject.R;
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
        public static final String SCHEME_WEB2 = "https:";
        public static final String SCHEME_WEB = "https:";
        public static final String SCHEME_AT = "user:";
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
        contextWeakReference = new WeakReference<>(context);
        spanableInfo = new SpannableStringBuilder(spanned);
        mContext = contextWeakReference.get();
        initLink();
        URLSpan[] urlSpans = spanableInfo.getSpans(0, spanableInfo.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_WEB2)) {
                int start = spanableInfo.getSpanStart(urlSpan);
                int end = spanableInfo.getSpanEnd(urlSpan);
                spanableInfo.removeSpan(urlSpan);
                SpannableStringBuilder urlSpannableString = getUrlTextSpannableString(context, urlSpan.getURL());
                spanableInfo.replace(start, end, urlSpannableString);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, start + urlSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_TOPIC)) {
                int start = spanableInfo.getSpanStart(urlSpan);
                int end = spanableInfo.getSpanEnd(urlSpan);
                spanableInfo.removeSpan(urlSpan);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(LinkPattern.SCHEME_AT)) {
                int start = spanableInfo.getSpanStart(urlSpan);
                int end = spanableInfo.getSpanEnd(urlSpan);
                spanableInfo.removeSpan(urlSpan);
                spanableInfo.setSpan(new ClickableLinkSpan(mContext, urlSpan.getURL(), color, onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return spanableInfo;
    }


    /**
     * 处理某种富文本的开关判断
     */
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
        Linkify.addLinks(spanableInfo, Pattern.compile(LinkPattern.REGEX_TOPIC), LinkPattern.SCHEME_TOPIC);
    }

    /**
     * 识别网址
     */
    private static void initWeb() {
        Linkify.addLinks(spanableInfo, Pattern.compile(LinkPattern.REGEX_WEB), LinkPattern.SCHEME_WEB2);
        Linkify.addLinks(spanableInfo, Pattern.compile(LinkPattern.REGEX_WEB2), LinkPattern.SCHEME_WEB2);
    }

    /**
     * 识别@
     */
    private static void initAt() {
        Linkify.addLinks(spanableInfo, Pattern.compile(LinkPattern.REGEXT_AT), LinkPattern.SCHEME_AT);
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

    /**
     * @param context
     * @param source
     * @return 转换成网页链接形式的链接
     */
    private static SpannableStringBuilder getUrlTextSpannableString(Context context, String source) {
        SpannableStringBuilder builder = new SpannableStringBuilder(source);
        String prefix = " ";
        builder.replace(0, prefix.length(), prefix);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_status_link);
        drawable.setBounds(0, 0, (int) mTextSize, (int) mTextSize);
        builder.setSpan(new VerticalImageSpan(drawable), prefix.length(), source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" 网页链接");
        // todo:  应当添加功能，可以将链接的标题返回作为文字提示
        return builder;
    }

    /**
     * 添加网页标题的需求见{@link #getUrlTextSpannableString}
     *
     * @param url
     * @return 网页标题
     */
    private String getWebTitle(String url) {
        String title = "";
        return title;
    }

}
