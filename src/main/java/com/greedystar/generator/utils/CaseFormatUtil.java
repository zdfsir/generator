package com.greedystar.generator.utils;

import com.google.common.base.CaseFormat;

/**
 * Copyright 2020 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2020/12/31 9:33
 * @Description:
 */
public class CaseFormatUtil {

    public static void main(String[] args) {
        System.out.println(String.format(">>> 小写连字符隔开To小写头字母驼峰(): %s", 小写连字符隔开To小写头字母驼峰("abc-db")));

    }

    /**
     * 大写首字母驼峰转小写首字母驼峰
     *
     * @param string 如：TestData
     * @return testData
     */
    public static String upperCamel2LowerCamel(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, string);
    }

    /**
     * @param string 如：test-data
     * @return testData
     */
    public static String 小写连字符隔开To小写头字母驼峰(String string) {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, string);
    }

    /**
     * @param string 如：test-data
     * @return TestData
     */
    public static String 小写连字符隔开To大写头字母驼峰(String string) {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, string);
    }

    /**
     * @param string 如：test_data
     * @return testData
     */
    public static String 小写下划线隔开To小写头字母驼峰(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string);
    }

    /**
     * @param string 如：test_data
     * @return TestData
     */
    public static String 小写下划线隔开To大写头字母驼峰(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }

    /**
     * @param string 如：TestData
     * @return test_data
     */
    public static String 大写头字母驼峰To小写下划线隔开(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    /**
     * @param string 如：testData
     * @return test_data
     */
    public static String 小写头字母驼峰To小写下划线隔开(String string) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    /**
     * @param string 如：TestData
     * @return test-data
     */
    public static String 大写头字母驼峰To小写连字符隔开(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, string);
    }

    /**
     * @param string 如：testData
     * @return test-data
     */
    public static String 小写头字母驼峰To小写连字符隔开(String string) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, string);
    }

    /**
     * @param string 如：testData
     * @return TestData
     */
    public static String 小写头字母驼峰To大写头字母驼峰(String string) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, string);
    }

    /**
     * 小写驼峰转小写.字符隔开
     * @param string 如：testData
     * @return test.data
     */
    public static String lowerCamel2LowerPoint(String string) {
        string = 小写头字母驼峰To小写下划线隔开(string);
        return string.replaceAll("_", ".");
    }

    /**
     * 大写驼峰转小写.字符隔开
     * @param string 如：TestData
     * @return test.data
     */
    public static String upperCamel2LowerPoint(String string) {
        string = 大写头字母驼峰To小写下划线隔开(string);
        return string.replaceAll("_", ".");
    }

    /**
     * 大写驼峰转小写/字符隔开
     * @param string 如：TestData
     * @return test.data
     */
    public static String upperCamel2LowerPath(String string) {
        string = 大写头字母驼峰To小写下划线隔开(string);
        return string.replaceAll("_", "/");
    }

    /**
     * 获得第一个单词
     *
     * @param string
     * @return
     */
    public static String getFirstWord(String string) {
        String[] arrays;
        if (string.indexOf(".") >= 0) {
            arrays = string.split("\\.");
        } else if (string.indexOf("-") >= 0) {
            arrays = string.split("-");
        } else if (string.indexOf("_") >= 0) {
            arrays = string.split("_");
        } else if (string.indexOf(",") >= 0) {
            arrays = string.split(",");
        } else {
            return string;
        }
        return arrays[0];
    }
}