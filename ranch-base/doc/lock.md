# 全局锁

全局锁主要应用于集群、分布式环境下，对同一关键数据进行悲观锁操作。

1、需要创建以下数据库表（MySQL版本）：

```sql
DROP TABLE IF EXISTS m_lock;
CREATE TABLE m_lock
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key CHAR(32) NOT NULL COMMENT '锁key',
  c_index BIGINT AUTO_INCREMENT NOT NULL COMMENT '序号',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_key(c_key) USING HASH,
  UNIQUE KEY uk_index(c_index)
) ENGINE=Memory AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```

2、设置数据源key：

```properties
## 设置全局锁使用的数据源key。
#ranch.lock.data-source.key = 
```

> 需要使用全局锁的应用必须使用同一个数据源配置。

3、通过LockHelper进行锁与解锁：

```java
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
```

> 锁与并发是互斥的，因此锁的范围应该尽可能小以获得更高的并发处理能力。
