package org.lpw.ranch.audit;

import org.lpw.ranch.recycle.RecycleTesterDao;

/**
 * @author lpw
 */
public interface AuditTesterDao<T extends AuditModel> extends RecycleTesterDao<T> {
    /**
     * 创建AuditModel持久化数据。
     *
     * @param i     序号。
     * @param audit 审核状态。
     * @return Model实例。
     */
    T create(int i, Audit audit);
}
