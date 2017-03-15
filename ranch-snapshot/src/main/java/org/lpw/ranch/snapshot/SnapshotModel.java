package org.lpw.ranch.snapshot;

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
@Component(SnapshotModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = SnapshotModel.NAME)
@Table(name = "t_snapshot")
public class SnapshotModel extends ModelSupport {
    static final String NAME = "ranch.snapshot";

    private String data; // 数据
    private String content; // 内容
    private Timestamp time; // 时间
    private String md5; // MD5值

    @Jsonable
    @Column(name = "c_data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Jsonable
    @Column(name = "c_md5")
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
