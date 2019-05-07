package org.lpw.ranch.editor.file;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.office.OfficeHelper;
import org.lpw.tephra.pdf.PdfHelper;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author lpw
 */
@Controller(FileModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Validator validator;
    @Inject
    private PdfHelper pdfHelper;
    @Inject
    private OfficeHelper officeHelper;
    @Inject
    private FileService fileService;

    @Override
    public String getKey() {
        return FileModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        switch (uploadReader.getParameter("type")) {
            case "pdf":
                return pdfHelper.is(uploadReader.getContentType(), uploadReader.getFileName());
            case "ppt":
                return officeHelper.isPpt(uploadReader.getContentType(), uploadReader.getFileName());
            case "word":
                return officeHelper.isWord(uploadReader.getContentType(), uploadReader.getFileName());
            default:
                return false;
        }
    }

    @Override
    public JSONObject settle(UploadReader uploadReader) throws IOException {
        return officeHelper.isWord(uploadReader.getContentType(), uploadReader.getFileName()) ? fileService.uploadNoCapture(uploadReader)
                : fileService.uploadCapture(uploadReader);
    }
}
