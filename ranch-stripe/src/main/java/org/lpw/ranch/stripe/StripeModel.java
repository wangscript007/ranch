package org.lpw.ranch.stripe;

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
@Component(StripeModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = StripeModel.NAME)
@Table(name = "t_stripe")
public class StripeModel extends ModelSupport {
    static final String NAME = "ranch.stripe";

    private String key; // 引用key
    private String name; // 名称
    private String email; // Email
    private String publishable; // 发行号
    private String secret; // 密钥

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
    @Column(name = "c_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Jsonable
    @Column(name = "c_publishable")
    public String getPublishable() {
        return publishable;
    }

    public void setPublishable(String publishable) {
        this.publishable = publishable;
    }

    @Jsonable
    @Column(name = "c_secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
