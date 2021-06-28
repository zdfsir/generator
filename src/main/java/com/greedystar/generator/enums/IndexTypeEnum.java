package com.greedystar.generator.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Copyright 2021 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2021/4/8 10:10
 * @Description: 索引类型
 */
@NoArgsConstructor
@AllArgsConstructor
public enum IndexTypeEnum {
    /**
     * 键（说明） [枚举编号：xxxx]
     */
    NO("", "无索引"),
    PRI("PRIMARY KEY", "主键"),
    UNI("UNIQUE", "唯一索引"),
    MUL("NORMAL", "普通索引"),
    ;

    /**
     * 索引约束类型
     */
    private String constraintType;

    /**
     * 说明
     */
    private String description;

    /**
     * 检查枚举类是否在预设参数中
     *
     * @param em   待查枚举
     * @param sets 预设允许的枚举数组
     * @return
     */
    public static boolean isInSet(IndexTypeEnum em, IndexTypeEnum... sets) {
        if (sets.length == 0) {
            return false;
        }
        for (IndexTypeEnum set : sets) {
            if (set.equals(em)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据枚举名获得对象
     *
     * @param name
     * @return
     */
    public static IndexTypeEnum getEnum(String name) {
        if (StrUtil.isBlank(name)) {
            return NO;
        }
        for (IndexTypeEnum enums : IndexTypeEnum.values()) {
            if (enums.name().equalsIgnoreCase(name)
                    || enums.getConstraintType().equals(name)) {
                return enums;
            }
        }
        return NO;
    }

    public String getConstraintType() {
        return constraintType;
    }

    /**
     * 获得枚举说明
     */
    public String getDescription() {
        return description;
    }
}