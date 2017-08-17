package org.lpw.ranch.captcha;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.ModelSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lpw
 */
@Component(CaptchaModel.NAME + ".model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = CaptchaModel.NAME)
@Table(name = "t_captcha")
public class CaptchaModel extends ModelSupport {
    static final String NAME = "ranch.captcha";

    private String key; // 引用key
    private String name; // 名称
    private int width; // 宽度
    private int height; // 高度
    private int fontMin; // 最小字号
    private int fontMax; // 最大字号
    private String chars; // 字符集
    private int length; // 字符数
    private int background; // 是否使用背景图片：0-否；1-是

    @Jsonable
    @Column(name = "c_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
    @Column(name = "c_font_min")
    public int getFontMin() {
        return fontMin;
    }

    public void setFontMin(int fontMin) {
        this.fontMin = fontMin;
    }

    @Jsonable
    @Column(name = "c_font_max")
    public int getFontMax() {
        return fontMax;
    }

    public void setFontMax(int fontMax) {
        this.fontMax = fontMax;
    }

    @Jsonable
    @Column(name = "c_chars")
    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    @Jsonable
    @Column(name = "c_length")
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Jsonable
    @Column(name = "c_background")
    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
