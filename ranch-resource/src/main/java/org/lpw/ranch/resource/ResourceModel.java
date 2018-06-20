package org.lpw.ranch.resource;

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
@Component(ResourceModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = ResourceModel.NAME)
@Table(name = "t_resource")
public class ResourceModel extends ModelSupport {
    static final String NAME = "ranch.resource";

    private String type; // 分类
    private int sort; // 顺序
    private String name; // 名称
    private String label; // 说明
    private String uri; // 资源URI地址
    private int width; // 图片宽
    private int height; // 图片高
    private String thumbnail; // 缩略图URI地址
    private String author; // 作者
    private int state; // 状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架
    private String user; // 用户
    private Timestamp time; // 时间

    @Jsonable
    @Column(name = "c_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Jsonable
    @Column(name = "c_sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
    @Column(name = "c_label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Jsonable
    @Column(name = "c_uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Jsonable
    @Column(name = "c_width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Jsonable
    @Column(name = "c_height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Jsonable
    @Column(name = "c_thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Jsonable
    @Column(name = "c_author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
    @Column(name = "c_user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
