package org.lpw.ranch.editor.buy;

import java.util.Map;

/**
 * @author lpw
 */
interface BuyDao {
    BuyModel find(String user, String editor);

    Map<String, Integer> count();

    void save(BuyModel buy);
}
