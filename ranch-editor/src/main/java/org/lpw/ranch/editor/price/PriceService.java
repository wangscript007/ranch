package org.lpw.ranch.editor.price;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PriceService {
    /**
     * 检索。
     *
     * @param type  类型。
     * @param group 分组。
     * @return 数据集。
     */
    JSONObject query(String type, String group);

    /**
     * 保存。
     *
     * @param price 价格。
     */
    void save(PriceModel price);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
