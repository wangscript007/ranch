package org.lpw.ranch.editor.buy;

import java.util.Map;

/**
 * @author lpw
 */
public interface BuyService {
    /**
     * 查找。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @return 如果存在则返回购买信息；否则返回null。
     */
    BuyModel find(String user, String editor);

    /**
     * 统计购买数。
     *
     * @return 购买数集。
     */
    Map<String, Integer> count();

    /**
     * 购买。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @param price  价格。
     */
    void create(String user, String editor, int price);
}
