package com.black.common.ui;

import android.widget.TextView;

import com.black.common.utils.Utils;

/**
 * Created by liumingkong on 15/6/4.
 */
public class SafeViewUtils {

    // 安全加载文本信息
    public static void setText(TextView textView, String text) {
        if (Utils.isNull(textView)) return;
        if (!Utils.isEmptyString(text)) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
    }
    public static void setText(TextView textView, int stringId) {
        if (Utils.isNull(textView)) return;
        textView.setText(stringId);
    }



}
