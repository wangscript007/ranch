package org.lpw.ranch.editor.download;

/**
 * @author lpw
 */
public interface DownloadService {
    /**
     * 下载数验证器Bean名称。
     */
    String VALIDATOR_COUNT = DownloadModel.NAME + ".validator.count";

    /**
     * 保存。
     *
     * @param editor    编辑器。
     * @param type      类型。
     * @param uri       源URI。
     * @param temporary 临时URI。
     */
    void save(String editor, String type, String uri, String temporary);
}
