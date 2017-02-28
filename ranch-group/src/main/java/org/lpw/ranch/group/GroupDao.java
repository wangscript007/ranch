package org.lpw.ranch.group;

/**
 * @author lpw
 */
interface GroupDao {
    GroupModel findById(String id);

    void save(GroupModel group);
}
