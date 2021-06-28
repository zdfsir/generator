package com.greedystar.generator.db;


import com.alibaba.fastjson.JSON;
import com.greedystar.generator.dto.TableDTO;
import com.greedystar.generator.dto.TableIndexColumnDTO;
import com.greedystar.generator.entity.ColumnInfo;
import com.greedystar.generator.enums.IndexTypeEnum;
import com.greedystar.generator.utils.ConfigUtil;
import com.greedystar.generator.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

/**
 * 数据库连接工具类
 *
 * @author GreedyStar
 * @since 2018/4/19
 */
@Slf4j
public class ConnectionUtil {

    /**
     * 查询一个表的索引信息
     */
    private static String SQL_SELECT_TABLE_INDEX_INFO = "SELECT \n" +
            "s.table_name, \n" +
            "s.index_name, \n" +
            "IFNULL(tc.constraint_type, 'NORMAL') as indexType, \n" +
            "GROUP_CONCAT( s.COLUMN_NAME ) AS 'indexColumns', \n" +
            "GROUP_CONCAT( s.SEQ_IN_INDEX ) AS 'indexSeq'\n" +
            "FROM `information_schema`.STATISTICS AS s\n" +
            "LEFT JOIN `information_schema`.TABLE_CONSTRAINTS tc\n" +
            "ON s.INDEX_NAME = tc.CONSTRAINT_NAME AND tc.TABLE_SCHEMA = '${databaseName}' AND tc.TABLE_NAME = '${tableName}'\n" +
            "WHERE s.TABLE_SCHEMA = '${databaseName}' AND s.TABLE_NAME = '${tableName}'\n" +
            "GROUP BY s.table_name, s.index_name;";
    private static String SqlServerTableRemarks = "";

    /**
     * 连接数据库的schema变量名
     */
    private static final String DB_SCHEMA_PARAM_KEY = "\\$\\{db.schema\\}";

    /**
     * 数据库连接
     */
    private Connection connection;

    /**
     * 初始化数据库连接
     *
     * @return 连接是否建立成功
     */
    public boolean initConnection() {
        try {
            Class.forName(DataBaseFactory.getDriver(ConfigUtil.getConfiguration().getDb().getUrl()));
            String schema = ConfigUtil.getConfiguration().getDb().getSchema();
            String url = ConfigUtil.getConfiguration().getDb().getUrl().replaceAll(DB_SCHEMA_PARAM_KEY, schema);
            String username = ConfigUtil.getConfiguration().getDb().getUsername();
            String password = ConfigUtil.getConfiguration().getDb().getPassword();
            Properties properties = new Properties();
            properties.put("user", username);
            properties.put("password", password == null ? "" : password);
            properties.setProperty("remarks", "true");
            properties.setProperty("useInformationSchema", "true");
            properties.setProperty("nullCatalogMeansCurrent", "true");
            connection = DriverManager.getConnection(url, properties);
            if (log.isDebugEnabled()) {
                log.debug(">>> getSchema [{}]", schema);
            }
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取表结构数据
     *
     * @param tableName 表名
     * @return 包含表结构数据的列表
     * @throws Exception
     */
    public TableDTO getMetaData(String tableName) throws Exception {
        if (!initConnection()) {
            throw new Exception("Failed to connect to database at url:" + ConfigUtil.getConfiguration().getDb().getUrl());
        }
        // 获取主键
        String primaryKey = getPrimaryKey(tableName);
        // 获取表注释
        String tableRemark = getTableRemark(tableName);
        // 获取列信息
        List<ColumnInfo> columnInfos = getColumnInfos(tableName, primaryKey, tableRemark);

        // 获取索引信息
        List<TableIndexColumnDTO> tableIndexColumnList = this.getIndexInfo(tableName);
        if (null != tableIndexColumnList && tableIndexColumnList.size() > 0) {
            if (log.isDebugEnabled()) {
                log.debug(">>> 读取表的索引信息 [{}]", JSON.toJSONString(tableIndexColumnList));
            }
            columnInfos.stream().forEach(columnInfo -> {
                TableIndexColumnDTO tableIndexColumns = null;
                boolean isBreak = false;
                for (TableIndexColumnDTO c : tableIndexColumnList) {
                    if (null == c.getIndexColumnList() || c.getIndexColumnList().size() == 0) {
                        break;
                    }
                    for (TableIndexColumnDTO.IndexColumn ic : c.getIndexColumnList()) {
                        if (ic.getColumnName().equals(columnInfo.getColumnName())) {
                            tableIndexColumns = c;
                            isBreak = true;
                            break;
                        }
                    }
                    if (isBreak) {
                        break;
                    }
                }
                if (null != tableIndexColumns) {
                    columnInfo.setIndexInfo(tableIndexColumns);
                }
            });
        }

        closeConnection();

        TableDTO tableDTO = TableDTO.builder()
             .primaryKey(primaryKey)
             .tableRemark(tableRemark)
                .tableInfoList(columnInfos)
                .tableIndexColumnList(tableIndexColumnList)
                .build();

        return tableDTO;
    }

    /**
     * 获取主键
     *
     * @param tableName
     * @return 主键名称
     * @throws SQLException
     */
    private String getPrimaryKey(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        // 获取主键
        ResultSet keyResultSet = databaseMetaData.getPrimaryKeys(DataBaseFactory.getCatalog(connection),
                DataBaseFactory.getSchema(connection), tableName);
        String primaryKey = null;
        if (keyResultSet.next()) {
            primaryKey = keyResultSet.getObject(4).toString();
        }
        keyResultSet.close();
        return primaryKey;
    }

    /**
     * 获取表注释
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private String getTableRemark(String tableName) throws SQLException {

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        // 获取表注释
        String tableRemark = null;
        ResultSet tableResultSet = databaseMetaData.getTables(DataBaseFactory.getCatalog(connection),
                DataBaseFactory.getSchema(connection), tableName, new String[]{"TABLE"});
        if (tableResultSet.next()) {
            tableRemark = StringUtil.isEmpty(tableResultSet.getString("REMARKS")) ?
                    "Unknown Table" : tableResultSet.getString("REMARKS");
        }
        tableResultSet.close();
        return tableRemark;
    }

    /**
     * 获取列信息
     *
     * @param tableName
     * @param primaryKey
     * @param tableRemark
     * @return
     * @throws Exception
     */
    private List<ColumnInfo> getColumnInfos(String tableName, String primaryKey, String tableRemark) throws Exception {
        // 获取列信息
        List<ColumnInfo> columnInfos = new ArrayList<>();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet columnResultSet = databaseMetaData.getColumns(DataBaseFactory.getCatalog(connection),
                DataBaseFactory.getSchema(connection), tableName, "%");
        int i = 1;
        while (columnResultSet.next()) {
            int columnType = columnResultSet.getType();
            if (log.isDebugEnabled()) {
                log.debug(">>> {} [{}] -> type: {}", i, columnResultSet.getObject(i), columnType);
            }
            boolean isPrimaryKey;
            if (columnResultSet.getString("COLUMN_NAME").equals(primaryKey)) {
                isPrimaryKey = true;
            } else {
                isPrimaryKey = false;
            }
            ColumnInfo info = new ColumnInfo(
                    columnResultSet.getString("COLUMN_NAME"),
                    columnResultSet.getInt("DATA_TYPE"),
                    StringUtil.isEmpty(columnResultSet.getString("REMARKS")) ? "Unknown" : columnResultSet.getString("REMARKS"),
                    tableRemark,
                    isPrimaryKey);
            columnInfos.add(info);
            i++;
        }
        columnResultSet.close();
        if (columnInfos.size() == 0) {
            closeConnection();
            throw new Exception("Can not find column information from table:" + tableName);
        }
        return columnInfos;
    }

    /*private void handleResultSet(ResultSet set) {
        try {
            ResultSetMetaData data = set.getMetaData();
            int j = 0;
            while (set.next()) {
                j++;
                //获得所有列的数目及实际列数
                int columnCount = data.getColumnCount();
                if (log.isDebugEnabled()) {
                    log.debug(">>> 遍历进度 [{}/{}]", j, columnCount);
                }
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    //获得指定列的列名
                    String columnName = data.getColumnName(i);
                    //获得指定列的列值
                    String columnValue = set.getString(i);
                    //获得指定列的数据类型
                    int columnType = data.getColumnType(i);
                    //获得指定列的数据类型名
                    String columnTypeName = data.getColumnTypeName(i);
                    //所在的Catalog名字
                    String catalogName = data.getCatalogName(i);
                    //对应数据类型的类
                    String columnClassName = data.getColumnClassName(i);
                    //在数据库中类型的最大字符个数
                    int columnDisplaySize = data.getColumnDisplaySize(i);
                    //默认的列的标题
                    String columnLabel = data.getColumnLabel(i);
                    //获得列的模式
                    String schemaName = data.getSchemaName(i);
                    //某列类型的精确度(类型的长度)
                    int precision = data.getPrecision(i);
                    //小数点后的位数
                    int scale = data.getScale(i);
                    //获取某列对应的表名
                    String tableName = data.getTableName(i);
                    // 是否自动递增
                    boolean isAutoInctement = data.isAutoIncrement(i);
                    //在数据库中是否为货币型
                    boolean isCurrency = data.isCurrency(i);
                    //是否为空
                    int isNullable = data.isNullable(i);
                    //是否为只读
                    boolean isReadOnly = data.isReadOnly(i);
                    //能否出现在where中
                    boolean isSearchable = data.isSearchable(i);
                    if (log.isDebugEnabled()) {
                        log.debug(">>> 列 {}", i);
                    }
                    ;
                    System.out.println("获得列" + i + "的字段名称:" + columnName);
                    System.out.println("获得列" + i + "的字段值:" + columnValue);
                    System.out.println("获得列" + i + "的类型,返回SqlType中的编号:" + columnType);
                    System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
                    System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
                    System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
                    System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
                    System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
                    System.out.println("获得列" + i + "的模式:" + schemaName);
                    System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
                    System.out.println("获得列" + i + "小数点后的位数:" + scale);
                    System.out.println("获得列" + i + "对应的表名:" + tableName);
                    System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
                    System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
                    System.out.println("获得列" + i + "是否为空:" + isNullable);
                    System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
                    System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }*/

    /**
     * 获取索引
     *
     * @param tableName
     * @return 主键名称
     * @throws SQLException
     */
    /*private List<ColumnInfo> getIndex(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("desc %s", tableName));
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        if (log.isDebugEnabled()) {
            log.debug(">>> columnCount [{}]", columnCount);
        }
        List<ColumnInfo> indexColumnList = new ArrayList<>();
        while (resultSet.next()) {
            String field = resultSet.getString("Field");
            String type = resultSet.getString("Type");
            IndexTypeEnum key = IndexTypeEnum.getEnum(resultSet.getString("Key"));
            if (log.isDebugEnabled()) {
                log.debug(">>> field [{}] type [{}] key [{}]", field, type, key);
            }
            ColumnInfo.ColumnInfoBuilder builder = ColumnInfo.builder()
                    .columnName(field)
                    .indexInfo(null);
            ColumnInfo columnInfo = builder.build();
            indexColumnList.add(columnInfo);

        }
        return indexColumnList;
    }*/

    /**
     * 读取表的索引信息
     *
     * @param tableName
     * @return 主键名称
     * @throws SQLException
     */
    private List<TableIndexColumnDTO> getIndexInfo(String tableName) throws SQLException {

        String schema = ConfigUtil.getConfiguration().getDb().getSchema();

        String sql = SQL_SELECT_TABLE_INDEX_INFO.replaceAll("\\$\\{databaseName\\}", schema).replaceAll("\\$\\{tableName\\}", tableName);

        if (log.isDebugEnabled()) {
            log.debug(">>> sql [{}]", sql);
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        if (log.isDebugEnabled()) {
            log.debug(">>> columnCount [{}]", columnCount);
        }
        List<TableIndexColumnDTO> tableColumnsDTOList = new ArrayList<>();
        if (log.isDebugEnabled()) {
            log.debug(">>> 表名 \t 列名\t 索引约束类型\t 索引列\t 索引列排序号");
        }
        while (resultSet.next()) {
            String table_name = resultSet.getString("table_name");
            String indexName = resultSet.getString("index_name");
            IndexTypeEnum indexType = IndexTypeEnum.getEnum(resultSet.getString("indexType"));
            String indexColumns = resultSet.getString("indexColumns");
            String indexSeq = resultSet.getString("indexSeq");
            if (log.isDebugEnabled()) {
                log.debug(">>> {} \t {}\t {}\t {}\t {}", table_name, indexName, indexType, indexColumns, indexSeq);
            }
            TableIndexColumnDTO.TableIndexColumnDTOBuilder builder = TableIndexColumnDTO.builder()
                    .tableName(tableName)
                    .indexName(indexName)
                    .indexType(indexType)
                    .indexColumns(indexColumns)
                    .indexSeq(indexSeq)
                    .indexType(indexType);
            TableIndexColumnDTO tableColumnsDTO = builder.build();
            tableColumnsDTOList.add(tableColumnsDTO);

        }
        return tableColumnsDTOList;
    }

    /**
     * 关闭数据库连接
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

}
