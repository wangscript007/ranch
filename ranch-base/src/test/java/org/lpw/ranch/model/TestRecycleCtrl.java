package org.lpw.ranch.model;

import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(TestModel.NAME + ".ctrl")
@Execute(name = "/model/", key = TestModel.NAME, code = "99")
public class TestRecycleCtrl extends RecycleCtrlSupport {
    @Autowired
    private TestRecycleService recycleService;

    @Override
    protected RecycleService getRecycleService() {
        return recycleService;
    }
}
