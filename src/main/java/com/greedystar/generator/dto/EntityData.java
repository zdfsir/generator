package com.greedystar.generator.dto;

import com.greedystar.generator.entity.Configuration;
import com.greedystar.generator.invoker.base.AbstractInvoker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Copyright 2021 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2021/4/7 19:05
 * @Description: 一个实体对应的所有参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityData implements Serializable {

    /**
     * 被召唤的对象（生成模板指定的表对象）
     */
    private AbstractInvoker invoker;

    /**
     * Generator 配置类
     */
    private Configuration Configuration;

    /**
     * 表名
     */
    private String TableName;

    /**
     * 类名
     */
    private String ClassName;

    /**
     * 类属性名
     */
    private String ClassAttrName;

    /**
     * Mapper类名
     */
    private String MapperClassName;

    /**
     * Mapper属性名
     */
    private String MapperAttrName;

    /**
     * InterfaceClassName
     */
    private String InterfaceClassName;

    /**
     * 表备注
     */
    private String Remarks;

    /**
     * Dao类名
     */
    private String DaoClassName;

    /**
     * Dao类属性名
     */
    private String DaoAttrName;
}