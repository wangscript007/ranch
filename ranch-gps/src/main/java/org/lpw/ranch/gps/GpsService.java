package org.lpw.ranch.gps;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GpsService {
    /**
     * 获取GPS逆向地址。
     *
     * @param lat 纬度。
     * @param lng 经度。
     * @return 逆向地址。
     */
    JSONObject address(String lat, String lng);
}
