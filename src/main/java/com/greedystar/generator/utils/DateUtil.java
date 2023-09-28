package com.greedystar.generator.utils;

import com.greedystar.generator.dto.LocalDateDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    public static void main(String[] args) {
        List<LocalDate> list = new ArrayList<>();
        list.add(LocalDate.of(2023, 1, 1));
        list.add(LocalDate.of(2023, 1, 5));

        for (LocalDate localDate : list) {
            LocalDateDTO date = getLocalDate(localDate);
            LocalDate weekBeginDate = getWeekBeginDateWithYearAndWeek(date.getYearAndWeek());
            System.out.printf("=>=>=>=>=>=> date.getYearAndWeek(): %s, weekBeginDate: %s\n", date.getYearAndWeek(), weekBeginDate);
        }

    }

    /**
     * 获取指定日期的详细对象
     *
     * @param localDate
     * @return
     */
    public static LocalDateDTO getLocalDate(LocalDate localDate) {
        DayOfWeek dayOfWeek = DayOfWeek.THURSDAY;
        return getLocalDate(localDate, dayOfWeek);
    }

    /**
     * 获取指定日期的详细对象
     *
     * @param localDate
     * @return
     */
    public static LocalDateDTO getLocalDate(LocalDate localDate, DayOfWeek dayOfWeek) {
        // 装箱返回参数
        LocalDateDTO result = new LocalDateDTO();
        result.setLocalDate(localDate);

        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        result.setYear(year);
        result.setMonthOfYear(month);
        result.setDayOfMonth(localDate.getDayOfMonth());

        // 重置周始后周的第1天
        LocalDate weekResetBeginDate = localDate.with(TemporalAdjusters.previousOrSame(dayOfWeek));
        result.setWeekBeginDate(weekResetBeginDate);
        result.setWeekEndDate(weekResetBeginDate.plusWeeks(1).minusDays(1));
        result.setYearOfWeekBegin(result.getWeekBeginDate().getYear());
        result.setYearOfWeekEnd(result.getWeekEndDate().getYear());
        result.setWeekOfYear(weekResetBeginDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));

        result.setMonthBeginDate(LocalDate.of(year, month, 1));
        result.setMonthEndDate(LocalDate.of(year, month, 1).plusMonths(1).minusDays(1));

        System.out.printf("=>=>=>=>=>=> date: %s, weekBeginDate: %s, weekEndDate: %s, yearAndWeek: %s\n", localDate, result.getWeekBeginDate(), result.getWeekEndDate(), result.getYearAndWeek());
        return result;
    }

    /**
     * 根据指定年周第1天日期
     *
     * @param yearAndWeek
     * @return
     */
    public static LocalDate getWeekBeginDateWithYearAndWeek(Integer yearAndWeek) {
        return getWeekBeginDateWithYearAndWeek(yearAndWeek, DayOfWeek.THURSDAY);
    }

    /**
     * 根据指定年周第1天日期
     *
     * @param yearAndWeek
     * @return
     */
    public static LocalDate getWeekBeginDateWithYearAndWeek(Integer yearAndWeek, DayOfWeek dayOfWeek) {
        int year = Integer.parseInt(yearAndWeek.toString().substring(0, 4));
        int weekOfYear = Integer.parseInt(yearAndWeek.toString().substring(4, 6));
        System.out.printf("=>=>=>=>=>=> year: %s, weekOfYear: %s\n", year, weekOfYear);
        LocalDate initYear = LocalDate.of(year, 1, 1);
        // 获得 1月1日所在周的第1天日期
        LocalDateDTO localDateDTO = getLocalDate(initYear, dayOfWeek);
        LocalDate weekBeginDate = localDateDTO.getWeekBeginDate();
        System.out.printf("=>=>=>=>=>=> weekBeginDate: %s\n", weekBeginDate);
        LocalDate yearAndWeekBeginDate = weekBeginDate.plusWeeks(weekOfYear - 1);
        System.out.printf("=>=>=>=>=>=> yearAndWeekBeginDate: %s\n", yearAndWeekBeginDate);
        return yearAndWeekBeginDate;
    }
}