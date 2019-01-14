package me.szlxty.check.bundle;

import me.szlxty.check.Checkers;
import me.szlxty.check.Constraint;
import me.szlxty.check.ConstraintFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 从数据库装载“简单对象”。其中约束代码为{@code codeColumn}列的值，约束描述为{@code briefColumn}列的值。
 * <p>默认的约束优先级是1</p>
 */
public class JdbcBundle extends AbstractBundle {
    private Logger logger = LoggerFactory.getLogger(JdbcBundle.class);
    public static final String DEFAULT_SQL_CLAUSE = "SELECT CODE, BRIEF FROM T_ERROR_DESCRIPTION";
    public static final String DEFAULT_CODE_COLUMN = "CODE";
    public static final String DEFAULT_BRIEF_COLUMN = "BRIEF";

    private DataSource dataSource;
    private String codeColumn = DEFAULT_CODE_COLUMN;
    private String briefColumn = DEFAULT_BRIEF_COLUMN;
    private String sqlClause = DEFAULT_SQL_CLAUSE;
    private int order = 1;

    @Override
    public List<Constraint> getConstraints() {
        Objects.requireNonNull(dataSource, "约束信息配置数据源不能为 null");
        List<Constraint> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sqlClause)) {
            while (resultSet.next()) {
                String code = resultSet.getString(codeColumn);
                String brief = resultSet.getString(briefColumn);

                if (result.stream().anyMatch(error -> error.code().equals(code))) {
                    logger.info("忽略已存在的约束配置: code={}，brief={}", code, brief);
                } else {
                    result.add(ConstraintFactory.create(code, brief, order));
                }
            }
        } catch (SQLException ex) {
            Checkers.invalid("获取约束配置规格过程约束", ex);
        }
        return result;
    }

    /**
     * 指定配置了约束规格的数据源。
     *
     * @param dataSource 用于查询约束规格配置内容的数据源。
     */
    public void setDataSource(DataSource dataSource) {
        Objects.requireNonNull(dataSource, "约束信息配置数据源不能为 null");
        this.dataSource = dataSource;
    }

    /**
     * 指定约束规格代码字段。
     *
     * @param codeColumn 表示约束规格代码的字段显示名称。如没指定，则为缺省 {@value #DEFAULT_CODE_COLUMN}。
     */
    public void setCodeColumn(String codeColumn) {
        Objects.requireNonNull(codeColumn, "约束代码字段不能为null");
        this.codeColumn = codeColumn;
    }

    /**
     * 指定约束规格概述字段。
     *
     * @param briefColumn 表示约束规格概要描述的字段显示名称。如没指定，则为缺省 {@value #DEFAULT_BRIEF_COLUMN}。
     */
    public void setBriefColumn(String briefColumn) {
        Objects.requireNonNull(briefColumn, "约束概述字段不能为null");
        this.briefColumn = briefColumn;
    }

    /**
     * 指定约束规格查找语句。
     *
     * @param sqlClause 用于查询约束规格配置内容的 SQL 语句。如没指定，则为缺省 {@value #DEFAULT_SQL_CLAUSE}。
     */
    public void setSqlClause(String sqlClause) {
        Objects.requireNonNull(sqlClause, "约束配置查询SQL语句不能为null");
        this.sqlClause = sqlClause;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
