package org.lpw.ranch.doc.relation;

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
@Component(RelationModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = RelationModel.NAME)
@Table(name = "t_doc_relation")
public class RelationModel extends ModelSupport {
    static final String NAME = "ranch.doc.refresh";

    private String doc; // 文档
    private String relate; // 关联文档
    private String type; // 类型：previous-前一篇；next-后一篇；alike-相似的
    private int sort; // 顺序

    @Jsonable
    @Column(name = "c_doc")
    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    @Jsonable
    @Column(name = "c_relate")
    public String getRelate() {
        return relate;
    }

    public void setRelate(String relate) {
        this.relate = relate;
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
    @Column(name = "c_sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
