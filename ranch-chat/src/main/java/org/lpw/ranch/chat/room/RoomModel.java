package org.lpw.ranch.chat.room;

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
@Component(RoomModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = RoomModel.NAME)
@Table(name = "t_chat_room")
public class RoomModel extends ModelSupport {
    static final String NAME = "chat.room";

    private String owner; // 所有者ID
    private String title; // 房间名称
    private int member; // 成员数
    private Timestamp newest; // 最新消息时间
    private Timestamp create; // 创建时间
    private String code; // 编号

    @Jsonable
    @Column(name = "c_owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Jsonable
    @Column(name = "c_title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Jsonable
    @Column(name = "c_member")
    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    @Jsonable
    @Column(name = "c_newest")
    public Timestamp getNewest() {
        return newest;
    }

    public void setNewest(Timestamp newest) {
        this.newest = newest;
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
    @Column(name = "c_code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
