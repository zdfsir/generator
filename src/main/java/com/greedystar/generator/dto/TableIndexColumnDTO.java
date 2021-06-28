package com.greedystar.generator.dto;

import cn.hutool.core.util.StrUtil;
import com.greedystar.generator.enums.IndexTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright 2021 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2021/4/8 22:19
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableIndexColumnDTO implements Serializable {
    /**
     * 表名 table_name
     */
    private String tableName;

    /**
     * 索引名 index_name
     */
    private String indexName;

    /**
     * 索引约束类型 indexType
     */
    private IndexTypeEnum indexType;

    /**
     * 索引列 indexColumns
     */
    private String indexColumns;

    /**
     * 索引列排序号 indexSeq
     */
    private String indexSeq;

    /**
     * 索引列
     */
//    private List<ColumnInfo> indexColumnList;

    /**
     * 索引列名
     */
    private List<IndexColumn> indexColumnList;

    public List<IndexColumn> getIndexColumnList() {
        if (StrUtil.isNotBlank(getIndexColumns()) && StrUtil.isNotBlank(getIndexSeq())) {
            String[] columns = getIndexColumns().split("[\\s,;:\\t]+");
            String[] seqs = getIndexSeq().split("[\\s,;:\\t]+");
            if (columns.length != seqs.length) {
                return null;
            }
            List<IndexColumn> list = new ArrayList<>();
            for (int i = 0; i < columns.length; i++) {
                IndexColumn indexColumn = IndexColumn.builder()
                        .columnName(columns[i])
                        .sort(Integer.parseInt(seqs[i]))
                        .build();
                list.add(indexColumn);
            }
            // 按照排序号正序排列
            list.sort(Comparator.comparing(IndexColumn::getSort));
            return list;
        }
        return indexColumnList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndexColumn {
        private String columnName;
        private int sort;
    }
}
