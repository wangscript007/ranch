package org.lpw.ranch.stripe.transaction;

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
@Component(TransactionModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = TransactionModel.NAME)
@Table(name = "t_stripe_transaction")
public class TransactionModel extends ModelSupport {
    static final String NAME = "ranch.stripe.transaction";

    private String key; // 引用key
    private String user; // 用户
    private String orderNo; // 订单号
    private String tradeNo; // 交易单号
    private int amount; // 金额，单位：分
    private String currency; // 币种
    private String response; // 验证结果
    private Timestamp create; // 创建时间
    private Timestamp finish; // 完成时间

    @Jsonable
    @Column(name = "c_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
    @Column(name = "c_order_no")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Jsonable
    @Column(name = "c_trade_no")
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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
    @Column(name = "c_currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Jsonable
    @Column(name = "c_response")
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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
    @Column(name = "c_finish")
    public Timestamp getFinish() {
        return finish;
    }

    public void setFinish(Timestamp finish) {
        this.finish = finish;
    }
}
