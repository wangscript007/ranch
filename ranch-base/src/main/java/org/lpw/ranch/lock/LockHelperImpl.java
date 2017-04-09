package org.lpw.ranch.lock;

import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LockModel.NAME + ".service")
public class LockHelperImpl implements LockHelper {
    @Inject
    private Digest digest;
    @Inject
    private Thread thread;
    @Inject
    private LockDao lockDao;

    @Override
    public String lock(String key, long wait) {
        if (key == null)
            return null;

        String md5 = digest.md5(key);
        LockModel lock = new LockModel();
        lock.setKey(md5);
        lockDao.save(lock);
        lock = lockDao.findById(lock.getId());
        for (long i = 0L; i < wait; i++) {
            LockModel model = lockDao.findByKey(md5);
            if (lock.getId().equals(model.getId()))
                return lock.getId();

            thread.sleep(1, TimeUnit.MilliSecond);
        }

        return null;
    }

    @Override
    public void unlock(String id) {
        lockDao.delete(id);
    }
}
