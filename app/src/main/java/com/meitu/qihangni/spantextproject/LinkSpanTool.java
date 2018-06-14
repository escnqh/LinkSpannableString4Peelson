package com.meitu.qihangni.spantextproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    public static void linkSpan(Spanned content, TextView textView, int color, @Nullable OnClickString onClickString, @Nullable Config config) {
        if (null != onClickString) {
            isCustomClick = true;
        }
        if (null != config) {
            configure(config);
        }
        textView.setText(getSpan(textView.getContext(), content, color, onClickString));
        textView.setMovementMethod(new LinkTouchMovementMethod());
    }

    public static SpannableStringBuilder getSpan(Context context, Spanned spanned, int color, @Nullable OnClickString onClickString) {
        contextWeakReference = new WeakReference<>(context);
        spanableInfo = new SpannableStringBuilder(spanned);
        spanableInfo_temp = new SpannableString(spanned);
        mContext = contextWeakReference.get();
        initLink();
        URLSpan[] urlSpans = spanableInfo_temp.getSpans(0, spanableInfo_temp.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            int start = spanableInfo_temp.getSpanStart(urlSpan);
            int end = spanableInfo_temp.getSpanEnd(urlSpan);
            spanableInfo.setSpan(new Clickable(mContext, urlSpan.getURL(), color,onClickString), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        String topic = "topic";
        Linkify.addLinks(spanableInfo_temp, Pattern.compile("#[^#]+#"), topic);
    }

    /**
     * 识别网址
     */
    private static void initWeb() {
        String scheme = "http://";
        Linkify.addLinks(spanableInfo_temp,
                Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"),
                scheme);

        String scheme2 = "https://";
        Linkify.addLinks(spanableInfo_temp,
                Pattern.compile("https://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"),
                scheme2);
    }

    /**
     * 识别@
     */
    private static void initAt() {
        String schemeuser = "user";
        Linkify.addLinks(spanableInfo_temp, Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}"), schemeuser);
    }

    interface OnClickString {
        void onClickString(String string);
    }

    static class Clickable extends ClickableSpan implements View.OnClickListener {
        private Context context;
        private String string;
        public int textcolor = 0xffeeeeee;
        private boolean mIsPressed;
        private LinkSpanTool.OnClickString mOnClickString;

        public Clickable(Context context, String string, int color,@Nullable LinkSpanTool.OnClickString mOnClickString) {
            this.context = context;
            this.string = string;
            this.mOnClickString = mOnClickString;

            //设置颜色
            if (color != -100) {
                textcolor = this.context.getResources().getColor(color);
            } else {
                textcolor = this.context.getResources().getColor(R.color.colorPrimary);
            }
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View v) {
            v.setTag("false");
            Log.i("111111111",string);
            if (LinkSpanTool.isCustomClick) {
                mOnClickString.onClickString(string);
            } else {

            }
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            // TODO Auto-generated method stub
            tp.setColor(mIsPressed ? context.getResources().getColor(android.R.color.black) : textcolor);
            tp.bgColor = mIsPressed ? 0xffeeeeee : context.getResources().getColor(android.R.color.transparent);
            tp.setUnderlineText(false);//设置下划线
            tp.clearShadowLayer();
        }
    }

    static class LinkTouchMovementMethod extends LinkMovementMethod {
        private Clickable mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Clickable touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private Clickable getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            Clickable[] link = spannable.getSpans(off, off, Clickable.class);
            Clickable touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
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


}
