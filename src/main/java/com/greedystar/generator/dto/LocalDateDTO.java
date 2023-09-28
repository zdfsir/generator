package com.greedystar.generator.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "LocalDateDTO", description = "")
public class LocalDateDTO {

    /**
     * 日期
     */
    private LocalDate localDate;

    /**
     * 年数
     */
    private Integer year;

    /**
     * 月份（1-12）
     */
    private Integer monthOfYear;

    /**
     * 日（月）
     */
    private Integer dayOfMonth;

    /**
     * 一周的开始日期
     */
    private LocalDate weekBeginDate;

    /**
     * 一周的截止日期
     */
    private LocalDate weekEndDate;

    /**
     * 周开始所在年
     */
    private Integer yearOfWeekBegin;

    /**
     * 周截止所在年
     */
    private Integer yearOfWeekEnd;

    /**
     * 周（年）
     */
    private Integer weekOfYear;

    /**
     * 一个月的开始日期
     */
    private LocalDate monthBeginDate;

    /**
     * 一个月的截止日期
     */
    private LocalDate monthEndDate;

    /**
     * 年月【get方法自动转换的属性】
     */
    private Integer yearAndMonth;

    /**
     * 年周【get方法自动转换的属性】
     */
    private Integer yearAndWeek;

    public Integer getYearAndMonth() {
        if (null == yearAndMonth && null != getMonthBeginDate()) {
            yearAndMonth = Integer.parseInt(String.format("%s%02d", getMonthBeginDate().getYear(), getMonthBeginDate().getMonthValue()));
        }
        return yearAndMonth;
    }

    public Integer getYearAndWeek() {
        if (null == yearAndWeek && null != getWeekBeginDate()) {
            yearAndWeek = Integer.parseInt(String.format("%s%02d", getWeekBeginDate().getYear(), getWeekOfYear()));
        }
        return yearAndWeek;
    }
}