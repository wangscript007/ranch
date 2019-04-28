package org.lpw.ranch.editor.download;

import org.lpw.ranch.editor.buy.BuyService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * @author lpw
 */
@Service(DownloadModel.NAME + ".service")
public class DownloadServiceImpl implements DownloadService, DateJob {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private BuyService buyService;
    @Inject
    private DownloadDao downloadDao;

    @Override
    public void save(String editor, String type, String uri, String temporary) {
        save(userHelper.id(), editor, type, uri, temporary);
    }

    @Override
    public void save(String user, String editor, String type, String uri, String temporary) {
        if (editor == null || buyService.find(user, editor) != null)
            return;

        DownloadModel download = new DownloadModel();
        download.setUser(user);
        download.setEditor(editor);
        download.setType(type);
        download.setUri(uri);
        download.setTemporary(temporary);
        download.setTime(dateTime.now());
        downloadDao.save(download);
    }

    @Override
    public void executeDateJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        downloadDao.delete(dateTime.getStart(calendar.getTime()));
    }
}
