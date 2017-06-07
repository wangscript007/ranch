package org.lpw.ranch.weixin;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.Memory;
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
@Component(WeixinModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = WeixinModel.NAME)
@Table(name = "t_weixin")
@Memory(name = "m_weixin")
public class WeixinModel extends ModelSupport {
    static final String NAME = "ranch.weixin";

    private String key; // 引用key
    private String name; // 名称
    private String appId; // APP ID
    private String secret; // 密钥
    private String token; // 验证Token
    private String mchId; // 商户ID
    private String mchKey; // 商户密钥
    private String accessToken; // Access Token
    private String jsapiTicket; // Jsapi Ticket
    private Timestamp time; // 更新时间

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
    @Column(name = "c_app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Jsonable
    @Column(name = "c_secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Jsonable
    @Column(name = "c_token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Jsonable
    @Column(name = "c_mch_id")
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Jsonable
    @Column(name = "c_mch_key")
    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    @Jsonable
    @Column(name = "c_access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Jsonable
    @Column(name = "c_jsapi_ticket")
    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
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
