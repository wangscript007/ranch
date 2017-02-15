package org.lpw.ranch.classify;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.ranch.recycle.RecycleModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(ClassifyModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ClassifyModel.NAME)
@Table(name = "t_classify")
public class ClassifyModel extends RecycleModelSupport {
    static final String NAME = "ranch.classify";

    private String code; // 编码
    private String key; // 关键词
    private String name; // 名称
    private String json; // JSON扩展

    @Jsonable
    @Column(name = "c_code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Jsonable
    @Column(name = "c_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
    @Column(name = "c_json")
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
