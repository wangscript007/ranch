package org.lpw.ranch.push.aliyun;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AliyunModel.NAME + ".ctrl")
@Execute(name = "/push/aliyun/", key = AliyunModel.NAME, code = "0")
public class AliyunCtrl {
    @Inject
    private Request request;
    @Inject
    private AliyunService aliyunService;
}
