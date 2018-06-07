package org.lpw.ranch.editor.resource;

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
@Component(ResourceModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ResourceModel.NAME)
@Table(name = "t_editor_resource")
public class ResourceModel extends ModelSupport {
    static final String NAME = "ranch.editor.resource";

    private String type; // 分类
    private int sort; // 顺序
    private String name; // 名称
    private String label; // 说明
    private String uri; // 资源URI地址
    private int state; // 状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架
    private String user; // 用户
    private String source; // 来源ID

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

    @Jsonable
    @Column(name = "c_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "c_uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Jsonable
    @Column(name = "c_state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
