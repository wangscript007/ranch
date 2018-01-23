package org.lpw.ranch.push.aliyun;

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
@Component(AliyunModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = AliyunModel.NAME)
@Table(name = "t_push_aliyun")
public class AliyunModel extends ModelSupport {
    static final String NAME = "ranch.push.aliyun";

    private String appCode; // APP编码
    private String keyId; // KEY ID
    private String keySecret; // KEY密钥
    private String appKey; // APP KEY
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_app_code")
    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Jsonable
    @Column(name = "c_key_id")
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Jsonable
    @Column(name = "c_key_secret")
    public String getKeySecret() {
        return keySecret;
    }

    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }

    @Jsonable
    @Column(name = "c_app_key")
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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
