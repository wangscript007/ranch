package org.lpw.ranch.editor.download;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(DownloadModel.NAME + ".ctrl")
@Execute(name = "/editor/download/", key = DownloadModel.NAME, code = "0")
public class DownloadCtrl {
    @Inject
    private Request request;
    @Inject
    private DownloadService downloadService;
}
