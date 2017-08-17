package org.lpw.ranch.dbtool.column;

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
@Component(ColumnModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ColumnModel.NAME)
@Table(name = "t_dbtool_column")
public class ColumnModel extends ModelSupport {
    static final String NAME = "ranch.dbtool.column";

    private String table; // 表
    private int sort; // 显示顺序
    private String name; // 名称
    private String type; // 数据类型
    private int nullable; // 是否可为NULL：0-否；1-是
    private String memo; // 备注

    @Jsonable
    @Column(name = "c_table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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
    @Column(name = "c_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Jsonable
    @Column(name = "c_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Jsonable
    @Column(name = "c_nullable")
    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    @Jsonable
    @Column(name = "c_memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
