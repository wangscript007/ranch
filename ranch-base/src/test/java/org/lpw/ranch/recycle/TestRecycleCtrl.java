package org.lpw.ranch.recycle;

import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TestRecycleModel.NAME + ".ctrl")
@Execute(name = "/recycle/", key = TestRecycleModel.NAME, code = "199")
public class TestRecycleCtrl extends RecycleCtrlSupport {
    @Inject
    private TestRecycleService recycleService;

    @Override
    protected RecycleService getRecycleService() {
        return recycleService;
    }
}
