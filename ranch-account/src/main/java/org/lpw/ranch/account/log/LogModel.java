package org.lpw.ranch.account.log;

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
@Table(name = "t_account_log")
public class LogModel extends ModelSupport {
    static final String NAME = "ranch.account.log";

    private String user; // 用户
    private String account; // 账户
    private String type; // 类型
    private int amount; // 数量
    private int balance; // 余额
    private int state; // 状态：0-待处理；1-审核通过；2-审核不通过；3-已完成
    private Timestamp start; // 开始时间
    private Timestamp end; // 结束时间
    private String json; // 扩展属性集

    @Jsonable
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Jsonable
    @Column(name = "c_account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
    @Column(name = "c_amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Jsonable
    @Column(name = "c_balance")
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
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
    @Column(name = "c_start")
    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    @Jsonable
    @Column(name = "c_end")
    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
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
