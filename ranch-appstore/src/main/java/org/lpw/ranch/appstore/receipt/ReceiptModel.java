package org.lpw.ranch.appstore.receipt;

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
@Component(ReceiptModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ReceiptModel.NAME)
@Table(name = "t_appstore_receipt")
public class ReceiptModel extends ModelSupport {
    static final String NAME = "ranch.appstore.receipt";

    private String productId; // 产品ID
    private int price; // 价格，单位：分
    private int quantity; // 数量
    private int amount; // 金额，单位：分
    private int status; // 状态
    private String md5; // 请求MD5
    private String request; // 请求数据
    private String response; // 返回数据
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Jsonable
    @Column(name = "c_price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Jsonable
    @Column(name = "c_quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    @Column(name = "c_status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Jsonable
    @Column(name = "c_md5")
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Jsonable
    @Column(name = "c_request")
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
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
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
