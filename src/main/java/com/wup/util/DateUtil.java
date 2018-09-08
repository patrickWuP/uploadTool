package com.wup.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 日期工具
 * @Project : imoocsecurity
 * @Program Name  : com.wup.util.DateUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class DateUtil {

    private static String dateAndTimeSqlPattern = "yyyyMMddHHmmss";


    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            System.out.println("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static String getCurrentDate(){
        return getDateTime(dateAndTimeSqlPattern,new Date());
    }

    public static void main(String[] args) {
        System.out.println(getDateTime(dateAndTimeSqlPattern,new Date()));
    }
}
