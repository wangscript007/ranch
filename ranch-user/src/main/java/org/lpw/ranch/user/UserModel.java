package org.lpw.ranch.user;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.ModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Component(UserModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = UserModel.NAME)
@Table(name = "t_user")
public class UserModel extends ModelSupport {
    static final String NAME = "ranch.user";

    private String password; // 密码
    private String secret; // 安全密码
    private String idcard; // 身份证号
    private String name; // 姓名
    private String nick; // 昵称
    private String mobile; // 手机号
    private String email; // Email地址
    private String portrait; // 头像
    private int gender; // 性别：0-未知；1-男；2-女
    private Date birthday; // 出生日期
    private String inviter; // 邀请人
    private int inviteCount; // 邀请人数
    private String code; // 唯一编码
    private Timestamp register; // 注册时间
    private int grade; // 等级：<50为用户；>=50为管理员；99为超级管理员
    private int state; // 状态：0-正常；1-禁用

    @Column(name = "c_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "c_secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Jsonable
    @Column(name = "c_idcard")
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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
    @Column(name = "c_nick")
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Jsonable
    @Column(name = "c_mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
    @Column(name = "c_portrait")
    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Jsonable
    @Column(name = "c_gender")
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Jsonable
    @Column(name = "c_birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Jsonable
    @Column(name = "c_inviter")
    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    @Jsonable
    @Column(name = "c_invite_count")
    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    @Jsonable
    @Column(name = "c_code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Jsonable
    @Column(name = "c_register")
    public Timestamp getRegister() {
        return register;
    }

    public void setRegister(Timestamp register) {
        this.register = register;
    }

    @Jsonable
    @Column(name = "c_grade")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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
