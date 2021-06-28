package com.greedystar.generator.task;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Singleton;
import com.greedystar.generator.dto.EntityData;
import com.greedystar.generator.entity.ColumnInfo;
import com.greedystar.generator.entity.Constant;
import com.greedystar.generator.invoker.base.AbstractInvoker;
import com.greedystar.generator.task.base.AbstractTask;
import com.greedystar.generator.utils.*;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author GreedyStar
 * @since 2018/4/20
 */
public class MapperXMLTask extends AbstractTask {

    private static DataReaderUtil dataReaderUtil = Singleton.get(DataReaderUtil.class, "MapperTask");

    public MapperXMLTask(AbstractInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void run() throws IOException, TemplateException {
        // 构造Mapper填充数据
        EntityData entityDataObject = dataReaderUtil.reader(null, invoker);
        Map<String, Object> data = Convert.toMap(String.class, Object.class, entityDataObject);
        data.put("TableName", invoker.getTableName());
        data.put("PrimaryKey", getPrimaryKeyColumnInfo(invoker.getTableInfos()).getColumnName());
        data.put("WhereId", "#{" + getPrimaryKeyColumnInfo(invoker.getTableInfos()).getPropertyName() + "}");
        data.put("PrimaryColumn", getPrimaryKeyColumnInfo(invoker.getTableInfos()));
        data.put("InsertProperties", insertProperties());
        data.put("ColumnMap", columnMap());
        data.put("ResultMap", resultMap());
        data.put("InsertBatchValues", insertBatchValues());
        data.put("InsertValues", insertValues());
        data.put("UpdateProperties", updateProperties());
        data.put("Joins", joins());
        if (!StringUtil.isEmpty(invoker.getRelationalTableName())) {
            // 多对多
            data.put("Association", "");
            data.put("Collection", collection());
        } else if (!StringUtil.isEmpty(invoker.getParentForeignKey())) {
            // 多对一
            data.put("Association", "");
            data.put("Collection", collection());
        } else if (!StringUtil.isEmpty(invoker.getForeignKey())) {
            // 一对多
            data.put("Association", association());
            data.put("Collection", "");
        } else {
            // 单表
            data.put("Association", "");
            data.put("Collection", "");
        }
        String filePath;
        if (ConfigUtil.getConfiguration().isMapperUnderSource()) {
            // mapper-under-source = true，表示将Mapper映射文件放在源文件目录下
            filePath = FileUtil.getSourcePath() + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName())
                    + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getMapperXML());
        } else {
            // 默认情况下，将Mapper映射文件放在resources下
            filePath = FileUtil.getResourcePath() + StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getMapperXML());
        }
        String fileName = ConfigUtil.getConfiguration().getName().getMapperXML().replace(Constant.PLACEHOLDER, invoker.getClassName()) + ".xml";
        // 生成Mapper文件
        FileUtil.generateToJava(FreemarkerConfigUtil.TYPE_MAPPER_XML, data, filePath, fileName);
    }

    /**
     * 获取主键列
     *
     * @param list
     * @return
     */
    private ColumnInfo getPrimaryKeyColumnInfo(List<ColumnInfo> list) {
        for (ColumnInfo columnInfo : list) {
            if (columnInfo.isPrimaryKey()) {
                return columnInfo;
            }
        }
        return null;
    }

    /**
     * 生成columnMap
     *
     * @return ColumnMap代码段
     */
    public String columnMap() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.getColumnName().equals(invoker.getForeignKey())) {
                return;
            }
            sb.append(index == 0 ? "" : Constant.SPACE_8);
            sb.append(String.format("`%s`.`%s`,\n", invoker.getTableName(), info.getColumnName()));
        }));
        if (invoker.getParentTableInfos() != null) {
            invoker.getParentTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
                sb.append(Constant.SPACE_8);
                if (!StringUtil.isEmpty(invoker.getRelationalTableName()) || !StringUtil.isEmpty(invoker.getParentForeignKey())) {
                    sb.append(String.format("`%s`.`%s` AS `%ss.%s`,\n", invoker.getParentTableName(), info.getColumnName(),
                            StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName()));
                } else {
                    sb.append(String.format("`%s`.`%s` AS `%s.%s`,\n", invoker.getParentTableName(), info.getColumnName(),
                            StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName()));
                }
            }));
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成resultMap
     *
     * @return ResultMap代码段
     */
    public String resultMap() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.getColumnName().equals(invoker.getForeignKey())) {
                return;
            }
            sb.append(index == 0 ? "" : Constant.SPACE_8);
            if (info.isPrimaryKey()) {
                sb.append(String.format("<id column=\"%s\" property=\"%s\" />\n", info.getColumnName(), info.getPropertyName()));
            } else {
                sb.append(String.format("<result column=\"%s\" property=\"%s\" />\n", info.getColumnName(), info.getPropertyName()));
            }
        }));
        return sb.toString();
    }

    /**
     * 生成association
     *
     * @return association代码段
     */
    public String association() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<association property=\"%s\" javaType=\"%s.%s\">\n", StringUtil.firstToLowerCase(invoker.getParentClassName()),
                ConfigUtil.getConfiguration().getPackageName() + "." + ConfigUtil.getConfiguration().getPath().getEntity(),
                invoker.getParentClassName()));
        invoker.getParentTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.isPrimaryKey()) {
                sb.append(Constant.SPACE_12).append(String.format("<id column=\"%s.%s\" property=\"%s\" />\n",
                        StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName(), info.getPropertyName()));
            } else {
                sb.append(Constant.SPACE_12).append(String.format("<result column=\"%s.%s\" property=\"%s\" />\n",
                        StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName(), info.getPropertyName()));
            }
        }));
        sb.append(Constant.SPACE_8).append("</association>");
        return sb.toString();
    }

    /**
     * 生成collection
     *
     * @return collection代码段
     */
    public String collection() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<collection property=\"%ss\" ofType=\"%s.%s\" >\n ", StringUtil.firstToLowerCase(invoker.getParentClassName()),
                ConfigUtil.getConfiguration().getPackageName() + "." + ConfigUtil.getConfiguration().getPath().getEntity(),
                invoker.getParentClassName()));
        invoker.getParentTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.isPrimaryKey()) {
                sb.append(Constant.SPACE_12).append(String.format("<id column=\"%ss.%s\" property=\"%s\" />\n",
                        StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName(), info.getPropertyName()));
            } else {
                sb.append(Constant.SPACE_12).append(String.format("<result column=\"%ss.%s\" property=\"%s\" />\n",
                        StringUtil.firstToLowerCase(invoker.getParentClassName()), info.getColumnName(), info.getPropertyName()));
            }
        }));
        sb.append(Constant.SPACE_8).append("</collection>");
        return sb.toString();
    }

    /**
     * 生成insertProperties
     *
     * @return insertProperties代码段
     */
    public String insertProperties() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            sb.append(index == 0 ? "" : Constant.SPACE_12);
            sb.append(String.format("`%s`,\n", info.getColumnName()));
        }));
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成insertValues
     *
     * @return insertValues代码段
     */
    public String insertValues() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            sb.append(index == 0 ? "" : Constant.SPACE_12);
            if (StringUtil.isEmpty(invoker.getRelationalTableName()) && !StringUtil.isEmpty(invoker.getForeignKey())) {
                if (info.getColumnName().equals(invoker.getForeignKey())) {
                    sb.append(String.format("#{%s.%s},\n", StringUtil.firstToLowerCase(invoker.getParentClassName()),
                            getPrimaryKeyColumnInfo(invoker.getParentTableInfos()).getPropertyName()));
                } else {
                    sb.append(String.format("#{%s},\n", info.getPropertyName()));
                }
            } else {
                sb.append(String.format("#{%s},\n", info.getPropertyName()));
            }
        }));
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成insertBatchValues
     *
     * @return insertBatchValues代码段
     */
    public String insertBatchValues() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            sb.append(index == 0 ? "" : Constant.SPACE_12);
            if (StringUtil.isEmpty(invoker.getRelationalTableName()) && !StringUtil.isEmpty(invoker.getForeignKey())) {
                if (info.getColumnName().equals(invoker.getForeignKey())) {
                    sb.append(String.format("#{%s.%s.%s},\n", StringUtil.firstToLowerCase(invoker.getClassName()),
                            StringUtil.firstToLowerCase(invoker.getParentClassName()),
                            getPrimaryKeyColumnInfo(invoker.getParentTableInfos()).getPropertyName()));
                } else {
                    sb.append(String.format("#{%s.%s},\n", StringUtil.firstToLowerCase(invoker.getClassName()), info.getPropertyName()));
                }
            } else {
                sb.append(String.format("#{%s.%s},\n", StringUtil.firstToLowerCase(invoker.getClassName()), info.getPropertyName()));
            }
        }));
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成updateProperties
     *
     * @return updateProperties代码段
     */
    public String updateProperties() {
        StringBuilder sb = new StringBuilder();
        invoker.getTableInfos().forEach(ForEachUtil.withIndex((info, index) -> {
            sb.append(index == 0 ? "" : Constant.SPACE_8);
            if (StringUtil.isEmpty(invoker.getRelationalTableName()) && !StringUtil.isEmpty(invoker.getForeignKey())) {
                if (info.getColumnName().equals(invoker.getForeignKey())) {
                    sb.append(String.format("`%s` = #{%s.%s},\n", info.getColumnName(),
                            StringUtil.firstToLowerCase(invoker.getParentClassName()),
                            getPrimaryKeyColumnInfo(invoker.getParentTableInfos()).getPropertyName()));
                } else {
                    sb.append(String.format("`%s` = #{%s},\n", info.getColumnName(), info.getPropertyName()));
                }
            } else {
                sb.append(String.format("`%s` = #{%s},\n", info.getColumnName(), info.getPropertyName()));
            }
        }));
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成joins代码段
     *
     * @return joins代码段
     */
    public String joins() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtil.isEmpty(invoker.getRelationalTableName())) {
            // 多对多
            sb.append(String.format("LEFT JOIN `%s` ON `%s`.`%s` = `%s`.`%s`", invoker.getRelationalTableName(),
                    invoker.getRelationalTableName(), invoker.getForeignKey(), invoker.getTableName(),
                    getPrimaryKeyColumnInfo(invoker.getTableInfos()).getColumnName()));
            sb.append("\n").append(Constant.SPACE_8);
            sb.append(String.format("LEFT JOIN `%s` ON `%s`.`%s` = `%s`.`%s`", invoker.getParentTableName(),
                    invoker.getParentTableName(), getPrimaryKeyColumnInfo(invoker.getParentTableInfos()).getColumnName(),
                    invoker.getRelationalTableName(), invoker.getParentForeignKey()));
        } else if (!StringUtil.isEmpty(invoker.getParentForeignKey())) {
            // 一对多
            sb.append(String.format("LEFT JOIN `%s` ON `%s`.`%s` = `%s`.`%s`", invoker.getParentTableName(),
                    invoker.getParentTableName(), invoker.getParentForeignKey(), invoker.getTableName(),
                    getPrimaryKeyColumnInfo(invoker.getTableInfos()).getColumnName()));
        } else if (!StringUtil.isEmpty(invoker.getForeignKey())) {
            // 多对一
            sb.append(String.format("LEFT JOIN `%s` ON `%s`.%s = `%s`.`%s`", invoker.getParentTableName(),
                    invoker.getParentTableName(), getPrimaryKeyColumnInfo(invoker.getParentTableInfos()).getColumnName(),
                    invoker.getTableName(), invoker.getForeignKey()));
        }
        return sb.toString();
    }

}
