package org.lpw.ranch.logger;

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
@Component(LoggerModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = LoggerModel.NAME)
@Table(name = "t_logger")
public class LoggerModel extends ModelSupport {
    static final String NAME = "ranch.logger";

    private String key; // 键
    private String p0; // 参数0
    private String p1; // 参数1
    private String p2; // 参数2
    private String p3; // 参数3
    private String p4; // 参数4
    private String p5; // 参数5
    private String p6; // 参数6
    private String p7; // 参数7
    private String p8; // 参数8
    private String p9; // 参数9
    private int state; // 状态
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Jsonable
    @Column(name = "c_p0")
    public String getP0() {
        return p0;
    }

    public void setP0(String p0) {
        this.p0 = p0;
    }

    @Jsonable
    @Column(name = "c_p1")
    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    @Jsonable
    @Column(name = "c_p2")
    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    @Jsonable
    @Column(name = "c_p3")
    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    @Jsonable
    @Column(name = "c_p4")
    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    @Jsonable
    @Column(name = "c_p5")
    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    @Jsonable
    @Column(name = "c_p6")
    public String getP6() {
        return p6;
    }

    public void setP6(String p6) {
        this.p6 = p6;
    }

    @Jsonable
    @Column(name = "c_p7")
    public String getP7() {
        return p7;
    }

    public void setP7(String p7) {
        this.p7 = p7;
    }

    @Jsonable
    @Column(name = "c_p8")
    public String getP8() {
        return p8;
    }

    public void setP8(String p8) {
        this.p8 = p8;
    }

    @Jsonable
    @Column(name = "c_p9")
    public String getP9() {
        return p9;
    }

    public void setP9(String p9) {
        this.p9 = p9;
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
    @Column(name = "c_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
