package org.lpw.ranch.editor.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.upload.UploadReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
public interface FileService {
    /**
     * 检索。
     *
     * @param editor 编辑器。
     * @return 数据集。
     */
    JSONArray query(String editor);

    /**
     * 统计下载数。
     *
     * @return 下载数集。
     */
    Map<String, Integer> count();

    /**
     * 获取编辑器ID集。
     *
     * @return 编辑器ID集。
     */
    Set<String> editors();

    /**
     * 保存。
     *
     * @param editor 编辑器ID。
     * @param type   类型。
     * @param file   文件。
     */
    void save(String editor, String type, File file);

    /**
     * 上传。
     *
     * @param uploadReader 文件读取器。
     * @return 上传结果。
     * @throws IOException 未处理IO异常。
     */
    JSONObject upload(UploadReader uploadReader) throws IOException;

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
