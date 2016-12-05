package org.lpw.ranch.recycle;

import org.lpw.tephra.dao.model.ModelSupport;

import javax.persistence.Column;

/**
 * @author lpw
 */
public class RecycleModelSupport extends ModelSupport implements RecycleModel {
    private int recycle; // 回收站：0-否，1-是

    @Column(name = "c_recycle")
    @Override
    public int getRecycle() {
        return recycle;
    }

    @Override
    public void setRecycle(int recycle) {
        this.recycle = recycle;
    }
}
