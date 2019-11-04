package com.zjg;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author zjg
 * @create 2019-10-22 16:20
 */
public class BlogTestNoSpring {
    //测试毫秒转为时间
    @Test
    public void test01() {
        long millis = System.currentTimeMillis();
        System.out.println(millis);
        Date date = new Date(millis);
        System.out.println(date);
        Instant instant = Instant.ofEpochMilli(millis);
        System.out.println(instant);
    }

    //测试毫秒转为时间 jdk8
    @Test
    public void test02() {
        LocalDateTime localDateTime3 = LocalDateTime.now();
        LocalDate.now();
        LocalTime.now();
        localDateTime3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(localDateTime3);
        LocalDateTime localDateTime4 = localDateTime3.minus(23, ChronoUnit.MONTHS);
        localDateTime4.atZone(ZoneId.systemDefault());
        localDateTime4 = localDateTime4.withHour(3);
        localDateTime4 = localDateTime4.withYear(2016);
        localDateTime4 = localDateTime4.with(ChronoField.MONTH_OF_YEAR,3);
        System.out.println("-------------------------------------------");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        System.out.println(localTime);
    }

    //测试毫秒转为时间 jdk8 2
    @Test
    public void test03() {
        Long newSecond1 = LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
        System.out.println(newSecond1);

        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println(zoneId);

        LocalDateTime now = LocalDateTime.now();
        ZoneOffset of = ZoneOffset.of("+8");

        ZoneOffset offset = OffsetDateTime.now().getOffset();
        System.out.println(offset);
    }

    //测试毫秒转为时间 jdk8 2
    @Test
    public void test04() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        System.out.println(localTime);

        long startTime = time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
        System.out.println(startTime);
    }

    //泛型类 instanceof
    @Test
    public void test05() {
        Map map = new HashMap();
        Map<Long, Long> map1 = new HashMap<>();

        System.out.println(map instanceof Map);
        System.out.println(map1 instanceof Map);
    }

    //map put方法, key不能重复
    @Test
    public void test06() {
        Map map = new HashMap();

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        for (Integer integer : list) {
            map.put(integer, 1);
        }
        System.out.println(map);
        System.out.println();

        for (Integer integer : list) {
            map.put(1, integer);
        }
        System.out.println(map);
    }


}
