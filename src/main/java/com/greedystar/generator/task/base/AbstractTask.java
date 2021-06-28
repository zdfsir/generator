package com.greedystar.generator.task.base;

import com.greedystar.generator.invoker.base.AbstractInvoker;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Serializable;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;

/**
 * @author GreedyStar
 * @since 2018/4/20
 */
public abstract class AbstractTask implements Serializable {
    protected AbstractInvoker invoker;

    protected static final List<JDBCType> TIME_JDBC_TYPE_LIST = Arrays.asList(new JDBCType[]{JDBCType.DATE, JDBCType.TIME, JDBCType.TIMESTAMP});

    public AbstractTask() {
    }

    /**
     * 执行任务
     *
     * @throws IOException 文件读写异常
     * @throws TemplateException 模板异常
     */
    public abstract void run() throws IOException, TemplateException;

}
