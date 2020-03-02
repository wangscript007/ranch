package org.lpw.ranch.user.crosier;

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
@Component(CrosierModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = CrosierModel.NAME)
@Table(name = "t_user_crosier")
public class CrosierModel extends ModelSupport {
    static final String NAME = "ranch.user.crosier";

    private int grade; // 等级
    private String uri; // URI
    private String parameter; // 参数

    @Jsonable
    @Column(name = "c_grade")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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
    @Column(name = "c_parameter")
    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
