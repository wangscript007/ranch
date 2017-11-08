package org.lpw.ranch.lock;

/**
 * 全局锁主要应用于集群、分布式环境下，对同一关键数据进行悲观锁操作。
 *
 * @author lpw
 */
public interface LockHelper {
    /**
     * 锁定。
     *
     * @param key   所key。
     * @param wait  等待时长，单位：毫秒。如果已被其他线程锁定则等待。
     * @param alive 有效时长,小于等于0则使用默认值5，单位：秒。如果超过有效时长未解锁的，将被自动解锁。
     * @return 锁定ID，如果锁定失败则返回null。
     */
    String lock(String key, long wait, int alive);

    /**
     * 解锁。
     *
     * @param id 锁定ID。
     */
    void unlock(String id);
}
