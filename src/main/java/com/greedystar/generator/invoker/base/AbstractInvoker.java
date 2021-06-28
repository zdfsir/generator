package com.greedystar.generator.invoker.base;

import com.greedystar.generator.db.ConnectionUtil;
import com.greedystar.generator.dto.TableDTO;
import com.greedystar.generator.dto.TableIndexColumnDTO;
import com.greedystar.generator.entity.ColumnInfo;
import com.greedystar.generator.task.base.AbstractTask;
import com.greedystar.generator.utils.TaskQueue;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author GreedyStar
 * @since 2018/9/5
 */
public abstract class AbstractInvoker implements Invoker {

    /**
     * 库名
     */
    protected String databaseName;
    /**
     * 主表名
     */
    protected String tableName;
    /**
     * 主类名
     */
    protected String className;
    /**
     * 父表名
     */
    protected String parentTableName;
    /**
     * 父类名
     */
    protected String parentClassName;
    /**
     * 外键列名
     */
    protected String foreignKey;
    /**
     * 关系表名
     */
    protected String relationalTableName;
    /**
     * 父表外键列名
     */
    protected String parentForeignKey;
    /**
     * 主表元数据
     */
    protected List<ColumnInfo> tableInfos;
    /**
     * 主表信息
     */
    protected TableDTO tableDTO;
    /**
     * 父表元数据
     */
    protected List<ColumnInfo> parentTableInfos;

    /**
     * 父表信息
     */
    protected TableDTO parentTableDTO;
    /**
     * 数据库连接工具
     */
    protected ConnectionUtil connectionUtil = new ConnectionUtil();
    /**
     * 任务队列
     */
    protected TaskQueue taskQueue = new TaskQueue();
    /**
     * 线程池
     */
    private ExecutorService executorPool = Executors.newFixedThreadPool(6);

    /**
     * 获取表元数据，模板方法，由子类实现
     *
     * @throws Exception 获取元数据失败则抛出异常
     */
    protected abstract void queryMetaData() throws Exception;

    /**
     * 初始化代码生成任务，模板方法，由子类实现
     */
    protected abstract void initTasks();

    /**
     * 开始生成代码
     */
    @Override
    public void execute() {
        try {
            queryMetaData();
            initTasks();
            while (!taskQueue.isEmpty()) {
                AbstractTask task = taskQueue.poll();
                executorPool.execute(() -> {
                    try {
                        task.run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TemplateException e) {
                        e.printStackTrace();
                    }
                });
            }
            executorPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setParentTableName(String parentTableName) {
        this.parentTableName = parentTableName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public void setRelationalTableName(String relationalTableName) {
        this.relationalTableName = relationalTableName;
    }

    public void setParentForeignKey(String parentForeignKey) {
        this.parentForeignKey = parentForeignKey;
    }

    public String getTableName() {
        return tableName;
    }

    public String getClassName() {
        return className;
    }

    public String getParentTableName() {
        return parentTableName;
    }

    public String getParentClassName() {
        return parentClassName;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public String getRelationalTableName() {
        return relationalTableName;
    }

    public String getParentForeignKey() {
        return parentForeignKey;
    }

    public List<ColumnInfo> getTableInfos() {
        return tableInfos;
    }

    public void setTableInfos(List<ColumnInfo> tableInfos) {
        this.tableInfos = tableInfos;
    }

    public List<ColumnInfo> getParentTableInfos() {
        return parentTableInfos;
    }

    public void setParentTableInfos(List<ColumnInfo> parentTableInfos) {
        this.parentTableInfos = parentTableInfos;
    }
}
