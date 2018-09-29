package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface AddressService {
    /**
     * 地址信息是否为当前用户所有验证器Bean名称。
     */
    String VALIDATOR_OWNER = AddressModel.NAME + ".validator.owner";

    /**
     * 检索当前用户地址集。
     *
     * @return 地址集；如果不存在则返回空JSON集。
     */
    JSONArray query();

    /**
     * 保存地址信息。
     * 如果ID不存在则新增；存在则更新。
     *
     * @param address 地址信息。
     * @return 地址信息JSON数据。
     */
    JSONObject save(AddressModel address);

    /**
     * 使用地址。
     *
     * @param id 地址ID值。
     * @return 地址信息JSON数据。
     */
    JSONObject use(String id);

    /**
     * 设置为默认地址。
     *
     * @param id 地址ID值。
     * @return 地址信息JSON数据。
     */
    JSONObject major(String id);

    /**
     * 删除地址。
     *
     * @param id 地址ID值。
     */
    void delete(String id);

    /**
     * 检索地址信息。
     *
     * @param id ID值。
     * @return 地址信息；如果不存在则返回null。
     */
    AddressModel findById(String id);
}
