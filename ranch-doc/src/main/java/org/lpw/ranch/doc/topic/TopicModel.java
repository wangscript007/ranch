package org.lpw.ranch.doc.topic;

import org.lpw.ranch.recycle.RecycleModelSupport;
import org.lpw.tephra.dao.model.Jsonable;
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
@Component(TopicModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TopicModel.NAME)
@Table(name = "t_doc_topic")
public class TopicModel extends RecycleModelSupport {
    static final String NAME = "ranch.doc.topic";

    private String doc; // 文档
    private String classify; // 分类
    private String author; // 作者
    private int sort; // 顺序
    private String subject; // 标题
    private String label; // 标签
    private String type; // 类型
    private Timestamp time; // 时间
    private int audit; // 审核：0-待审核；1-审核通过；2-审核不通过

    @Jsonable
    @Column(name = "c_doc")
    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    @Jsonable
    @Column(name = "c_classify")
    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    @Jsonable
    @Column(name = "c_author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
    @Column(name = "c_subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Jsonable
    @Column(name = "c_label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Jsonable
    @Column(name = "c_audit")
    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }
}
