package org.lpw.ranch.recycle;

import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(TestRecycleModel.NAME + ".ctrl")
@Execute(name = "/recycle/", key = TestRecycleModel.NAME, code = "99")
public class TestRecycleCtrl extends RecycleCtrlSupport {
    @Autowired
    private TestRecycleService recycleService;

    @Override
    protected RecycleService getRecycleService() {
        return recycleService;
    }
}
