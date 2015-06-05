package com.black.common.utils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by liumingkong on 15/5/7.
 * 签名生成算法
 */
public class ParamsUtils {

    public static String getParamSignature(Map<String, String> params) {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            baseString.append(param.getKey()).append("=").append(param.getValue());
        }
        return baseString.toString();
    }
}
