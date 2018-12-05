package org.lpw.ranch.editor.speech;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface SpeechDao {
    PageList<SpeechModel> query(String user, Timestamp[] times, int pageSize, int pageNum);

    SpeechModel findById(String id);

    void save(SpeechModel speech);

    String getData(String id);

    void setData(String id, String data);

    void delete(String id);
}
