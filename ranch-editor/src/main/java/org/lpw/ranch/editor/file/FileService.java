package org.lpw.ranch.editor.file;

import java.io.File;

/**
 * @author lpw
 */
public interface FileService {
    /**
     * 保存。
     *
     * @param editor 编辑器ID。
     * @param type   类型。
     * @param file   文件。
     */
    void save(String editor, String type, File file);

    /**
     * 保存。
     *
     * @param editor 编辑器ID。
     * @param type   类型。
     * @param uri    URI地址。
     * @param size   文件大小。
     */
    void save(String editor, String type, String uri, long size);

    /**
     * 下载。
     *
     * @param editor 编辑器ID。
     * @param type   类型。
     * @param email  Email地址。
     * @return 下载链接。
     */
    String download(String editor, String type, String email);
}
