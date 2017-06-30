package org.lpw.ranch.link;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface LinkDao {
    PageList<LinkModel> query1(String type, String id1, int pageSize, int pageNum);

    PageList<LinkModel> query2(String type, String id2, int pageSize, int pageNum);

    int count1(String type, String id1);

    int count2(String type, String id2);

    LinkModel find(String type, String id1, String id2);

    void save(LinkModel link);

    void delete(String type, String id1, String id2);
}
