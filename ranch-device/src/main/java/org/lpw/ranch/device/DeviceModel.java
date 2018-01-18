package org.lpw.ranch.device;

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
@Component(DeviceModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = DeviceModel.NAME)
@Table(name = "t_device")
public class DeviceModel extends ModelSupport {
    static final String NAME = "ranch.device";

    private String user; // 用户
    private String appCode; // APP编码
    private String type; // 类型：android、ios
    private String macId; // Mac ID
    private String version; // 版本号
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_app_code")
    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Jsonable
    @Column(name = "c_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Jsonable
    @Column(name = "c_mac_id")
    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    @Jsonable
    @Column(name = "c_version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
