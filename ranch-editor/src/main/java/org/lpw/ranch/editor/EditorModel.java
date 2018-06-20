package org.lpw.ranch.editor;

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
@Component(EditorModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = EditorModel.NAME)
@Table(name = "t_editor")
public class EditorModel extends ModelSupport {
    static final String NAME = "ranch.editor";

    private int template; // 模板：0-否；1-是
    private String type; // 类型
    private int sort; // 顺序
    private String name; // 名称
    private String keyword; // 关键词
    private int width; // 宽度
    private int height; // 高度
    private String image; // 预览图
    private String screenshot; // 主截图
    private int state; // 状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架；5-已删除
    private String json; // 扩展属性集
    private Timestamp create; // 创建时间
    private Timestamp modify; // 修改时间

    @Jsonable
    @Column(name = "c_template")
    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
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
    @Column(name = "c_keyword")
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
    @Column(name = "c_image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Jsonable
    @Column(name = "c_screenshot")
    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    @Jsonable
    @Column(name = "c_state")
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Jsonable(extend = true)
    @Column(name = "c_json")
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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
    @Column(name = "c_modify")
    public Timestamp getModify() {
        return modify;
    }

    public void setModify(Timestamp modify) {
        this.modify = modify;
    }
}
