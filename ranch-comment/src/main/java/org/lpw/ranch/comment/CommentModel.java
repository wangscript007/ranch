package org.lpw.ranch.comment;

import org.lpw.ranch.audit.AuditModelSupport;
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
@Component(CommentModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = CommentModel.NAME)
@Table(name = "t_comment")
public class CommentModel extends AuditModelSupport {
    static final String NAME = "ranch.comment";

    private String key; // 服务KEY
    private String owner; // 所有者ID
    private String author; // 作者ID
    private String subject; // 标题
    private String label; // 标签
    private String content; // 内容
    private int score; // 评分
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Jsonable
    @Column(name = "c_owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
    @Column(name = "c_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Jsonable
    @Column(name = "c_score")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Jsonable
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
