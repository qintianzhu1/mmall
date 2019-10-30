package com.mmall.controller.backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Test {

  public static void main(String[] args) {
//    byte[] input = new byte[] { (byte) 0xe4, (byte) 0xb8, (byte) 0xad, 0x21 };
//    String b64encoded = Base64.getEncoder().encodeToString(input);
//    String b64encoded2 = Base64.getEncoder().withoutPadding().encodeToString(input);
//    System.out.println(b64encoded);
//    System.out.println(b64encoded2);
//    byte[] output = Base64.getDecoder().decode(b64encoded2);
//    System.out.println(Arrays.toString(output));


      LocalDate d = LocalDate.now(); // 当前日期
      LocalTime t = LocalTime.now(); // 当前时间
      LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
      System.out.println(d); // 严格按照ISO 8601格式打印
      System.out.println(t); // 严格按照ISO 8601格式打印
      System.out.println(dt); // 严格按照ISO 8601格式打印

  }


}
