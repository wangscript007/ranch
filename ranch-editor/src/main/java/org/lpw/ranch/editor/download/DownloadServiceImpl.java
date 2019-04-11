package org.lpw.ranch.editor.download;

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
    private DownloadDao downloadDao;

    @Override
    public void save(String editor, String type, String uri, String temporary) {
        DownloadModel download = new DownloadModel();
        download.setUser(userHelper.id());
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
