package org.lpw.ranch.link;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
interface LinkDao {
    PageList<LinkModel> query1(String type, String id1, int pageSize, int pageNum);

    PageList<LinkModel> query2(String type, String id2, int pageSize, int pageNum);

    PageList<LinkModel> query1(String type, Set<String> id1s, int pageSize, int pageNum);

    PageList<LinkModel> query2(String type, Set<String> id2s, int pageSize, int pageNum);

    int count(String type, String id1, String id2);

    LinkModel find(String type, String id1, String id2);

    void save(LinkModel link);

    void delete(String type, String id1, String id2);
}
