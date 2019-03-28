package org.lpw.ranch.editor.file;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Map;

/**
 * @author lpw
 */
interface FileDao {
    PageList<FileModel> query(String editor);

    FileModel find(String editor, String type);

    Map<String, Integer> count();

    void save(FileModel file);
}
