package org.lpw.ranch.editor.speech;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface SpeechDao {
    PageList<SpeechModel> query(String user, int state, Timestamp[] times, int pageSize, int pageNum);

    PageList<SpeechModel> query(int state);

    SpeechModel findById(String id);

    void save(SpeechModel speech);

    void delete(String id);
}
