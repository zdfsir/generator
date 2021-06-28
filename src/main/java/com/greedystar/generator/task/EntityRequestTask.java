package com.greedystar.generator.task;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Singleton;
import com.greedystar.generator.dto.EntityData;
import com.greedystar.generator.entity.ColumnInfo;
import com.greedystar.generator.entity.Constant;
import com.greedystar.generator.entity.IdStrategy;
import com.greedystar.generator.entity.Mode;
import com.greedystar.generator.invoker.base.AbstractInvoker;
import com.greedystar.generator.task.base.AbstractTask;
import com.greedystar.generator.utils.*;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author GreedyStar
 * @since 2018/4/20
 */
@Slf4j
public class EntityRequestTask extends AbstractTask {
    /**
     * 业务表元数据
     */
    private List<ColumnInfo> tableInfos;
    /**
     * 任务模式
     */
    private Mode mode;

    private static DataReaderUtil dataReaderUtil = Singleton.get(DataReaderUtil.class, "EntityRequestTask");

    public EntityRequestTask(Mode mode, AbstractInvoker invoker) {
        this.mode = mode;
        this.invoker = invoker;
        if (Mode.ENTITY_MAIN.equals(mode)) {
            this.tableInfos = invoker.getTableInfos();
        } else if (Mode.ENTITY_PARENT.equals(mode)) {
            this.tableInfos = invoker.getParentTableInfos();
        }
    }

    @Override
    public void run() throws IOException, TemplateException {
        EntityData entityDataObject = dataReaderUtil.reader(mode, invoker);
        Map<String, Object> data = Convert.toMap(String.class, Object.class, entityDataObject);
        data.put("Properties", entityProperties(invoker));
        data.put("Methods", entityMethods(invoker));
        String filePath = FileUtil.getSourcePath() + StringUtil.package2Path(ConfigUtil.getConfiguration().getPackageName());
        String fileName = ConfigUtil.getConfiguration().getName().getEntity().replace(Constant.PLACEHOLDER, entityDataObject.getClassName());
        if (log.isDebugEnabled()) {
//            log.debug(">>> {} 元数据 [{}]" , this.getClass().getName(), JSON.toJSONString(data, true));
        }
        // 生成文件
        FileUtil.generateToJava(FreemarkerConfigUtil.TYPE_ENTITY_SEARCH, data,
                String.format("%s%s", filePath, StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getEntitySearchDTO())),
                String.format("%sSearchDTO.java", fileName));
        FileUtil.generateToJava(FreemarkerConfigUtil.TYPE_ENTITY_REQUEST_DTO, data,
                String.format("%s%s", filePath, StringUtil.package2Path(ConfigUtil.getConfiguration().getPath().getEntityRequestDTO())),
                String.format("%sRequestDTO.java", fileName));
    }

    /**
     * 生成实体类属性字段
     *
     * @param invoker 执行器
     * @return 属性代码段
     */
    public String entityProperties(AbstractInvoker invoker) {
        StringBuilder sb = new StringBuilder();
        tableInfos.forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.getColumnName().equals(invoker.getForeignKey())
                    || info.isPrimaryKey()
                    || TIME_JDBC_TYPE_LIST.contains(info.getColumnType())) {
                sb.append("\n");
                return;
            }
            sb.append(index == 0 ? "" : Constant.SPACE_4);
            generateRemarks(sb, info);
            generateORMAnnotation(sb, info);
            sb.append(Constant.SPACE_4).append(String.format("private %s %s;\n", info.getPropertyType(), info.getPropertyName()));
            sb.append("\n");
        }));
        // 生成父表实体类时，直接截断后续生成依赖关系的代码
        if (Mode.ENTITY_PARENT.equals(mode)) {
            return sb.toString();
        }
        if (!StringUtil.isEmpty(invoker.getRelationalTableName()) || !StringUtil.isEmpty(invoker.getParentForeignKey())) {
            // 多对多 or 一对多
            sb.append(Constant.SPACE_4).append(String.format("private List<%s> %ss;\n", invoker.getParentClassName(),
                    StringUtil.firstToLowerCase(invoker.getParentClassName())));
            sb.append("\n");
        } else if (!StringUtil.isEmpty(invoker.getForeignKey())) {
            // 多对一
            sb.append(Constant.SPACE_4).append(String.format("private %s %s;\n", invoker.getParentClassName(),
                    StringUtil.firstToLowerCase(invoker.getParentClassName())));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 生成实体类存取方法
     *
     * @param invoker 执行器
     * @return 方法代码段
     */
    public String entityMethods(AbstractInvoker invoker) {
        if (ConfigUtil.getConfiguration().isLombokEnable()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        tableInfos.forEach(ForEachUtil.withIndex((info, index) -> {
            if (info.getColumnName().equals(invoker.getForeignKey())) {
                return;
            }
            String setter = String.format("public void set%s (%s %s) { this.%s = %s; } \n\n", StringUtil.firstToUpperCase(info.getPropertyName()),
                    info.getPropertyType(), info.getPropertyName(), info.getPropertyName(), info.getPropertyName());
            sb.append(index == 0 ? "" : Constant.SPACE_4).append(setter);
            String getter = null;
            if (info.getPropertyType().equals("boolean") || info.getPropertyType().equals("Boolean")) {
                getter = String.format("public %s is%s () { return this.%s; } \n\n", info.getPropertyType(),
                        StringUtil.firstToUpperCase(info.getPropertyName()), info.getPropertyName());
            } else {
                getter = String.format("public %s get%s () { return this.%s; } \n\n", info.getPropertyType(),
                        StringUtil.firstToUpperCase(info.getPropertyName()), info.getPropertyName());
            }
            sb.append(Constant.SPACE_4).append(getter);
        }));
        // 生成父表实体类时，直接截断后续生成依赖关系的代码
        if (Mode.ENTITY_PARENT.equals(mode)) {
            return sb.toString();
        }
        if (!StringUtil.isEmpty(invoker.getRelationalTableName()) || !StringUtil.isEmpty(invoker.getParentForeignKey())) {
            // 多对多
            String setter = String.format("public void set%ss (List<%s> %ss) { this.%ss = %ss; }\n\n", invoker.getParentClassName(),
                    invoker.getParentClassName(), StringUtil.firstToLowerCase(invoker.getParentClassName()),
                    StringUtil.firstToLowerCase(invoker.getParentClassName()), StringUtil.firstToLowerCase(invoker.getParentClassName()));
            sb.append(Constant.SPACE_4).append(setter);
            String getter = String.format("public List<%s> get%ss () { return this.%ss; }\n\n", invoker.getParentClassName(),
                    invoker.getParentClassName(), StringUtil.firstToLowerCase(invoker.getParentClassName()));
            sb.append(Constant.SPACE_4).append(getter);
        } else if (!StringUtil.isEmpty(invoker.getForeignKey())) {
            // 多对一
            String setter = String.format("public void set%s (%s %s) { this.%s = %s; }\n\n", invoker.getParentClassName(),
                    invoker.getParentClassName(), StringUtil.firstToLowerCase(invoker.getParentClassName()),
                    StringUtil.firstToLowerCase(invoker.getParentClassName()), StringUtil.firstToLowerCase(invoker.getParentClassName()));
            sb.append(Constant.SPACE_4).append(setter);
            String getter = String.format("public %s get%s () { return this.%s; }\n\n", invoker.getParentClassName(), invoker.getParentClassName(),
                    StringUtil.firstToLowerCase(invoker.getParentClassName()));
            sb.append(Constant.SPACE_4).append(getter);
        }
        return sb.toString();
    }

    /**
     * 为实体属性生成注释
     *
     * @param sb   StringBuilder对象
     * @param info 列属性
     */
    public void generateRemarks(StringBuilder sb, ColumnInfo info) {
        sb.append("/**").append("\n");
        sb.append(Constant.SPACE_4).append(" * ").append(info.getRemarks()).append("\n");
        sb.append(Constant.SPACE_4).append(" */").append("\n");
    }

    /**
     * 为实体属性生成swagger注解
     * 我们不建议在entity（do）中使用swagger注解，在dto和vo中使用swagger注解更为优雅
     *
     * @param sb   StringBuilder对象
     * @param info 列属性
     */
    public void generateSwaggerAnnotation(StringBuilder sb, ColumnInfo info) {
        if (!ConfigUtil.getConfiguration().isSwaggerEnable()) {
            return;
        }
        sb.append(String.format("@ApiModelProperty(value = \"%s\", dataType = \"%s\")",
                info.getRemarks(), info.getPropertyType()));
        sb.append("\n");
    }

    /**
     * 为实体属性生成Orm框架（jpa/mybatis-plus）注解
     *
     * @param sb   StringBuilder对象
     * @param info 列属性
     */
    public void generateORMAnnotation(StringBuilder sb, ColumnInfo info) {
        if (ConfigUtil.getConfiguration().isMybatisPlusEnable()) {
            if (info.isPrimaryKey()) {
                if (ConfigUtil.getConfiguration().getIdStrategy() == null ||
                        ConfigUtil.getConfiguration().getIdStrategy() == IdStrategy.AUTO) {
                    sb.append(Constant.SPACE_4)
                            .append(String.format("@ApiModelProperty(value=\"%s\")\n", info.getRemarks()));
                } else if (ConfigUtil.getConfiguration().getIdStrategy() == IdStrategy.UUID) {
                }
            } else {
                sb.append(Constant.SPACE_4)
                        .append(String.format("@ApiModelProperty(value=\"%s\", notes=\"%s\", required = %s)\n", info.getRemarks(), info.getIsUnique() ? "UniqueKey": "", info.getIsRequired() ? "true" : "false"));
            }
        } else if (ConfigUtil.getConfiguration().isJpaEnable()) {
            if (info.isPrimaryKey()) {
                if (ConfigUtil.getConfiguration().getIdStrategy() == null || ConfigUtil.getConfiguration().getIdStrategy() == IdStrategy.AUTO) {
                    sb.append(Constant.SPACE_4).append("@Id\n");
                    sb.append(Constant.SPACE_4).append("@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                } else if (ConfigUtil.getConfiguration().getIdStrategy() == IdStrategy.UUID) {
                    sb.append(Constant.SPACE_4).append("@Id\n");
                    sb.append(Constant.SPACE_4).append("@GeneratedValue(generator = \"uuidGenerator\")\n");
                    sb.append(Constant.SPACE_4).append("@GenericGenerator(name = \"uuidGenerator\", strategy = \"uuid\")\n");
                }
            }
            sb.append(Constant.SPACE_4).append(String.format("@Column(name = \"%s\")\n", info.getColumnName()));
        }
    }

}
