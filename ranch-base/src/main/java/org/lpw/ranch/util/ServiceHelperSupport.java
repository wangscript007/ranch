package org.lpw.ranch.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.util.Validator;

import javax.inject.Inject;

/**
 * @author lpw
 */
public abstract class ServiceHelperSupport {
    @Inject
    protected Validator validator;
    @Inject
    protected Carousel carousel;
    private String getKey;

    /**
     * 获取信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    public JSONObject get(String id) {
        if (getKey == null)
            getKey = getKey() + ".get";

        return carousel.get(getKey, id);
    }

    /**
     * 填充信息。
     *
     * @param array 要填充的数据集。
     * @param names 要填充的属性名称集。
     * @return 填充后的数据集。
     */
    public JSONArray fill(JSONArray array, String[] names) {
        if (validator.isEmpty(array) || validator.isEmpty(names))
            return array;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String name : names) {
                if (!object.containsKey(name))
                    continue;

                String id = object.getString(name);
                if (!validator.isEmpty(id))
                    object.put(name, get(id));
            }
        }

        return array;
    }

    /**
     * 获取服务key。
     *
     * @return 服务key。
     */
    protected abstract String getKey();
}
