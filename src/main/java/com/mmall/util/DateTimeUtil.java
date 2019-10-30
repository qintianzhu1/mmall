package com.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author kong
 */
public class DateTimeUtil {

  private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static Date strToDate(String dateTimeStr,String formatStr){
    DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(formatStr);
    DateTime dateTime = dateTimeFormat.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }


  public static String dateToStr(Date date, String formatStr){
    if(date == null){
      return StringUtils.EMPTY;
    }
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(formatStr);
  }

  public static Date strToDate(String dateTimeStr){
    DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(STANDARD_FORMAT);
    DateTime dateTime = dateTimeFormat.parseDateTime(dateTimeStr);
    return dateTime.toDate();
  }


  public static String dateToStr(Date date){
    if(date == null){
      return StringUtils.EMPTY;
    }
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(STANDARD_FORMAT);
  }

}
