package org.lpw.ranch.dbtool.table;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.ModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(TableModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TableModel.NAME)
@Table(name = "t_dbtool_table")
public class TableModel extends ModelSupport {
    static final String NAME = "ranch.dbtool.table";

    private String schema; // 数据库
    private int sort; // 显示顺序
    private String group; // 分组ID
    private String name; // 名称
    private String memo; // 备注
    private int columns; // 列数量

    @Jsonable
    @Column(name = "c_schema")
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Jsonable
    @Column(name = "c_sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Jsonable
    @Column(name = "c_group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Jsonable
    @Column(name = "c_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Jsonable
    @Column(name = "c_memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Jsonable
    @Column(name = "c_columns")
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
