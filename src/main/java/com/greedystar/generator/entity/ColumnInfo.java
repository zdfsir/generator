package com.greedystar.generator.entity;

import cn.hutool.core.collection.CollectionUtil;
import com.greedystar.generator.dto.TableIndexColumnDTO;
import com.greedystar.generator.enums.IndexTypeEnum;
import com.greedystar.generator.utils.ConvertorUtil;
import com.greedystar.generator.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * 数据列实体
 *
 * @author GreedyStar
 * @since 2018/4/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo implements Serializable {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列名 -- 属性名
     */
    private String propertyName;
    /**
     * 数据列类型
     */
    private JDBCType columnType;
    /**
     * 数据列类型 -- Java类型
     */
    private String propertyType;
    /**
     * 列备注
     */
    private String remarks;
    /**
     * 表备注
     */
    private String tableRemarks;
    /**
     * 是否主键
     */
    private boolean isPrimaryKey;
    /**
     * 是否唯一索引列
     */
    private boolean isUnique;

    /**
     * 索引信息
     */
    private TableIndexColumnDTO indexInfo;

    public ColumnInfo(String columnName, int columnType, String remarks, String tableRemarks, boolean isPrimaryKey) {
        this.columnName = columnName;
        this.propertyName = StringUtil.columnName2PropertyName(columnName);
        this.columnType = JDBCType.valueOf(columnType);
        this.propertyType = ConvertorUtil.parseTypeFormSqlType(JDBCType.valueOf(columnType));
        this.remarks = remarks;
        this.tableRemarks = tableRemarks;
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isUnique() {
        if (null != indexInfo
                // 有唯一索引标记
                && IndexTypeEnum.UNI.equals(indexInfo.getIndexType())
                // 唯一索引列不为空
                && CollectionUtil.isNotEmpty(indexInfo.getIndexColumnList())
                // 唯一索引列只有一个，即当前列
                && indexInfo.getIndexColumnList().size() == 1) {
            return true;
        }
        return false;
    }
}
