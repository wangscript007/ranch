package org.lpw.ranch.push.log;

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
@Component(LogModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = LogModel.NAME)
@Table(name = "t_push_log")
public class LogModel extends ModelSupport {
    static final String NAME = "ranch.push.log";

    private String user; // 用户
    private String receiver; // 接收地址
    private String appCode; // APP编码
    private String sender; // 推送器
    private String push; // 推送配置
    private String args; // 参数集
    private Timestamp time; // 时间
    private int state; // 状态：0-新建；1-已推送；2-已阅读；3-推送失败

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_receiver")
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
    @Column(name = "c_sender")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Jsonable
    @Column(name = "c_push")
    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    @Jsonable
    @Column(name = "c_args")
    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Jsonable
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Jsonable
    @Column(name = "c_state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
