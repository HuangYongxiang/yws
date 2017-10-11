package com.example.wangchuang.yws.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaoguangqi on 16/7/13.
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str == null || "null".equals(str) || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        if (str == null || "null".equals(str) || str.length() == 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 将字符串转成数字
     *
     * @param str
     * @return
     */
    public static int stringToInt(String str) {
        int i = 0;
        if (str != null) {
            try {
                i = Integer.parseInt(str.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        }
        return i;
    }

    /**
     * 将字符串转成数字
     *
     * @param str
     * @return
     */
    public static double stringToDouble(String str) {
        double i = 0;
        if (str != null) {
            try {
                i = Double.parseDouble(str.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        }
        return i;
    }

    /**
     * 将字符串转成数字
     *
     * @param str
     * @return
     */
    public static long stringToLong(String str) {
        long i = 0;
        if (str != null) {
            try {
                i = Long.parseLong(str.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        }
        return i;
    }

    public static float stringToFloat(String str) {
        float i = 0;
        if (str != null) {
            try {
                i = Float.parseFloat(str.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        }
        return i;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (isEmpty(s)) {
            return "0";
        } else {
            if (s.indexOf(".") > 0) {
                s = s.replaceAll("0+?$", "");// 去掉多余的0
                s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
            }
            return s;
        }
    }



    public static boolean isPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
       // String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        String regExp = "^1[34578]\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 去标点
     * @param s
     * @return
     */
    public static String format(String s){
        String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");

        return str;
    }

    public static boolean isIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        String regExp = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[x|X|\\d]$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idCard);
        return m.find();
    }


    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    public static String idCardDeal(String idCard) {
        StringBuffer sb = new StringBuffer(idCard);
        sb.replace(6, 16, "**********");
        return sb.toString();
    }

    public static String getPrefix(String name) {
        File f = new File(name);
        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    public static String subSting(String string){
        String tempString = string;
        if(isNotEmpty(string)&&string.length()>10){
            tempString = string.substring(string.length()-10,string.length());
        }
        return tempString;

    }

}
