package com.meitu.qihangni.spantextproject.LinkSpannableString;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

/**
 * 扩展更多富文本形式应当实现此接口，并且在初始化富文本工具时将实现类添加到{@link SpanModeManager}的list中
 *
 * @author nqh 2018/7/12
 */
public interface BaseSpanMode {
    SpannableStringBuilder linkSpan(SpannableStringBuilder spannableInfo, Context context, int color, @Nullable OnClickString onClickString);
}
