package com.greedystar.generator.dto;

import com.greedystar.generator.entity.ColumnInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright 2021 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2021/4/8 10:09
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO implements Serializable {
    /**
     * 表主键
     */
    private String primaryKey;

    /**
     * 表备注
     */
    private String tableRemark;
    /**
     * 主表元数据
     */
    protected List<ColumnInfo> tableInfoList;
    /**
     * 主表索引数据
     */
    protected List<TableIndexColumnDTO> tableIndexColumnList;
}
