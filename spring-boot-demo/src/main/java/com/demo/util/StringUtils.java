package com.demo.util;

/**
 * 字符串工具类
 * 提供常用的字符串处理方法
 * 
 * @author Demo
 * @version 1.0
 * @since 2026-01-07
 */
public class StringUtils {

    private StringUtils() {
        // 私有构造方法，防止实例化
    }

    /**
     * 判断字符串是否为空或null
     * 
     * @param str 待判断的字符串
     * @return 如果为空或null返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空且不为null
     * 
     * @param str 待判断的字符串
     * @return 如果不为空且不为null返回true，否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白（包含空格、制表符等）
     * 
     * @param str 待判断的字符串
     * @return 如果为空白返回true，否则返回false
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空白
     * 
     * @param str 待判断的字符串
     * @return 如果不为空白返回true，否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 反转字符串
     * 
     * @param str 待反转的字符串
     * @return 反转后的字符串，如果输入为null则返回null
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 将字符串首字母大写
     * 
     * @param str 待处理的字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * 将字符串首字母小写
     * 
     * @param str 待处理的字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 统计子字符串在字符串中出现的次数
     * 
     * @param str    主字符串
     * @param substr 子字符串
     * @return 出现次数
     */
    public static int countOccurrences(String str, String substr) {
        if (isEmpty(str) || isEmpty(substr)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substr, index)) != -1) {
            count++;
            index += substr.length();
        }
        return count;
    }

    /**
     * 重复字符串指定次数
     * 
     * @param str   待重复的字符串
     * @param times 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int times) {
        if (str == null || times <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 左填充字符串到指定长度
     * 
     * @param str       原字符串
     * @param size      目标长度
     * @param padChar   填充字符
     * @return 填充后的字符串
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            str = "";
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return repeat(String.valueOf(padChar), pads) + str;
    }

    /**
     * 右填充字符串到指定长度
     * 
     * @param str       原字符串
     * @param size      目标长度
     * @param padChar   填充字符
     * @return 填充后的字符串
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            str = "";
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return str + repeat(String.valueOf(padChar), pads);
    }

    /**
     * 判断字符串是否只包含数字
     * 
     * @param str 待判断的字符串
     * @return 如果只包含数字返回true，否则返回false
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 安全地截取字符串
     * 
     * @param str   原字符串
     * @param start 起始位置
     * @param end   结束位置
     * @return 截取后的字符串
     */
    public static String safeSubstring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        return str.substring(start, end);
    }
}

