package org.lpw.ranch.editor.download;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author lpw
 */
interface DownloadDao {
    Set<String> editors(String user, Timestamp time);

    void save(DownloadModel download);

    void delete(Timestamp time);
}
