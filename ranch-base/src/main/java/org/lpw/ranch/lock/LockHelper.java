package org.lpw.ranch.lock;

/**
 * @author lpw
 */
public interface LockHelper {
    /**
     * 锁定。
     *
     * @param key  所key。
     * @param wait 等待时长，单位：毫秒。如果已被其他线程锁定则等待。
     * @return 锁定ID，如果锁定失败则返回null。
     */
    String lock(String key, long wait);

    /**
     * 解锁。
     *
     * @param id 锁定ID。
     */
    void unlock(String id);
}
