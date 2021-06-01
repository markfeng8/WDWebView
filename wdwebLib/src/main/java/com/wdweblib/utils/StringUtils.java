package com.wdweblib.utils;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 13:41
 */
public class StringUtils {

    /**
     * str 不等于:null , "" ,"null" 返回true
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !(str == null || "".equals(str));
    }
}
