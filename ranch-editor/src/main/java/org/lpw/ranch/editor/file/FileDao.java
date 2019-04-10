package org.lpw.ranch.editor.file;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
interface FileDao {
    PageList<FileModel> query(String editor);

    FileModel findById(String id);

    FileModel find(String editor, String type);

    Map<String, Integer> count();

    Set<String> editors();

    void save(FileModel file);
}
