package com.greedystar.generator.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.greedystar.generator.dto.EntityData;
import com.greedystar.generator.entity.ColumnInfo;
import com.greedystar.generator.entity.Constant;
import com.greedystar.generator.entity.Mode;
import com.greedystar.generator.invoker.base.AbstractInvoker;
import lombok.extern.slf4j.Slf4j;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright 2021 @http://www.rzhkj.com
 *
 * @Author borong
 * @Date 2021/4/7 19:02
 * @Description: 数据读取工具类
 */
@Slf4j
public class DataReaderUtil {

    public DataReaderUtil() {
    }

    private static final List<JDBCType> TIME_JDBC_TYPE_LIST = Arrays.asList(new JDBCType[]{JDBCType.DATE, JDBCType.TIME, JDBCType.TIMESTAMP});

    private String consumer;

    public DataReaderUtil(String consumer) {
        if (log.isDebugEnabled()) {
            log.debug(">>> consumer [{}]", consumer);
        }
        this.consumer = consumer;
    }

    /**
     * 读取表获得对应实体数据
     *
     * @param invoker
     * @return
     */
    public EntityData reader(Mode mode, AbstractInvoker invoker) {
        // 表名
        String className = null;
        // 表备注
        String remarks = null;
        if (null == mode || Mode.ENTITY_MAIN.equals(mode)) {
            className = ConfigUtil.getConfiguration().getName().getEntity().replace(Constant.PLACEHOLDER, invoker.getClassName());
            remarks = invoker.getTableInfos().get(0).getTableRemarks();
        } else if (Mode.ENTITY_PARENT.equals(mode)) {
            className = ConfigUtil.getConfiguration().getName().getEntity().replace(Constant.PLACEHOLDER, invoker.getParentClassName());
            remarks = invoker.getParentTableInfos().get(0).getTableRemarks();
        }

        EntityData.EntityDataBuilder builder = EntityData.builder()
                .invoker(invoker)
                .Configuration(ConfigUtil.getConfiguration())
                .TableName(invoker.getTableName())
                .ClassName(className)
                .ClassAttrName(StringUtil.firstToLowerCase(ConfigUtil.getConfiguration().getName().getEntity().replace(Constant.PLACEHOLDER, invoker.getClassName())))
                .MapperClassName(ConfigUtil.getConfiguration().getName().getMapper().replace(Constant.PLACEHOLDER, invoker.getClassName()))
                .InterfaceClassName(ConfigUtil.getConfiguration().getName().getInterf().replace(Constant.PLACEHOLDER, invoker.getClassName()))
                .MapperAttrName(StringUtil.firstToLowerCase(ConfigUtil.getConfiguration().getName().getMapper().replace(Constant.PLACEHOLDER, invoker.getClassName())))
                .Remarks(remarks)
                .uniqueColumnList(this.queryUniqueColumnList(invoker.getTableInfos()))
                .dateTimeColumnList(this.queryDateTimeColumnList(invoker.getTableInfos()))
                ;

        EntityData entityData = builder.build();

        if (log.isDebugEnabled()) {
            log.debug(">>> {}\t 读表 {} \t 获得对应实体数据：{}", consumer, invoker.getTableName(), JSON.toJSONString(entityData));
        }

        return entityData;
    }

    /**
     * 获得唯一索引列的集合
     *
     * @param tableInfos
     * @return
     */
    private List<ColumnInfo> queryUniqueColumnList(List<ColumnInfo> tableInfos) {
        if (CollectionUtil.isEmpty(tableInfos)) {
            return null;
        }
        List<ColumnInfo> list = tableInfos.parallelStream().filter(o -> o.getIsUnique()).collect(Collectors.toList());
        return list;
    }

    /**
     * 获得日期时间列的集合
     *
     * @param tableInfos
     * @return
     */
    private List<ColumnInfo> queryDateTimeColumnList(List<ColumnInfo> tableInfos) {
        if (CollectionUtil.isEmpty(tableInfos)) {
            return null;
        }

        List<ColumnInfo> list = tableInfos.parallelStream().filter(o -> TIME_JDBC_TYPE_LIST.contains(o.getColumnType())).collect(Collectors.toList());
        return list;
    }

}