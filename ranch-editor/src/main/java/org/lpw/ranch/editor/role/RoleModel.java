package org.lpw.ranch.editor.role;

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
@Component(RoleModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = RoleModel.NAME)
@Table(name = "t_editor_role")
public class RoleModel extends ModelSupport {
    static final String NAME = "ranch.editor.role";

    private String user; // 用户
    private String editor; // 编辑器
    private int type; // 类型：0-所有者；1-可编辑；2-仅浏览
    private String password; // 访问密码
    private int template; // 编辑器模板
    private String etype; // 编辑器类型
    private int state; // 编辑器状态
    private Timestamp modify; // 编辑器修改时间
    private Timestamp create; // 创建时间

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_editor")
    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
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
    @Column(name = "c_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Jsonable
    @Column(name = "c_template")
    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    @Jsonable
    @Column(name = "c_etype")
    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
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
    @Column(name = "c_modify")
    public Timestamp getModify() {
        return modify;
    }

    public void setModify(Timestamp modify) {
        this.modify = modify;
    }

    @Jsonable
    @Column(name = "c_create")
    public Timestamp getCreate() {
        return create;
    }

    public void setCreate(Timestamp create) {
        this.create = create;
    }
}
