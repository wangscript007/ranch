package org.lpw.ranch.form.user;

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
@Component(UserModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = UserModel.NAME)
@Table(name = "t_form_user")
public class UserModel extends ModelSupport {
    static final String NAME = "ranch.form.user";

    private String user; // 用户
    private String form; // 表单
    private int type; // 类型：0-所有者；1-可设计；2-可编辑；3-可分享；4-只读
    private Timestamp create; // 创建时间
    private Timestamp join; // 加入时间

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_form")
    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Jsonable
    @Column(name = "c_type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    @Column(name = "c_join")
    public Timestamp getJoin() {
        return join;
    }

    public void setJoin(Timestamp join) {
        this.join = join;
    }
}
