package org.lpw.ranch.link;

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
@Component(LinkModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = LinkModel.NAME)
@Table(name = "t_link")
public class LinkModel extends ModelSupport {
    static final String NAME = "ranch.link";

    private String type; // 类型
    private String id1; // 连接ID1
    private String id2; // 连接ID2
    private String json; // 属性集
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Jsonable
    @Column(name = "c_id1")
    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    @Jsonable
    @Column(name = "c_id2")
    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
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
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
