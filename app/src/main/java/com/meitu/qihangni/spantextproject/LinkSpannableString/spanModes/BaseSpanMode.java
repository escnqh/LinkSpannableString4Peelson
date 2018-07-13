package com.meitu.qihangni.spantextproject.LinkSpannableString.spanModes;

import android.content.Context;
import android.text.SpannableStringBuilder;
import com.meitu.qihangni.spantextproject.LinkSpannableString.SpanModeManager;

/**
 * 扩展更多富文本形式应当实现此接口，并且在初始化富文本工具时将实现类添加到{@link SpanModeManager}的list中
 *
 * @author nqh 2018/7/12
 */
public interface BaseSpanMode {
    SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context);
}
