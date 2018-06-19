package org.lpw.ranch.editor.element;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.ModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Component(ElementModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ElementModel.NAME)
@Table(name = "t_editor_element")
public class ElementModel extends ModelSupport {
    static final String NAME = "ranch.editor.element";

    private String editor; // 编辑器
    private String parent; // 父元素
    private int sort; // 顺序
    private String json; // 扩展属性集
    private String text; // 文本
    private Timestamp create; // 创建时间
    private long modify; // 修改时间

    @Jsonable
    @Column(name = "c_editor")
    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Jsonable
    @Column(name = "c_parent")
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Jsonable
    @Column(name = "c_sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Jsonable(extend = true)
    @Column(name = "c_json")
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Jsonable
    @Column(name = "c_text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Jsonable
    @Column(name = "c_create")
    public Timestamp getCreate() {
        return create;
    }

    public void setCreate(Timestamp create) {
        this.create = create;
    }

    @Jsonable
    @Column(name = "c_modify")
    public long getModify() {
        return modify;
    }

    public void setModify(long modify) {
        this.modify = modify;
    }
}
