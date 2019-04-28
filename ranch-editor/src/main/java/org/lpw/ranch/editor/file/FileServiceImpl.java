package org.lpw.ranch.editor.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.download.DownloadService;
import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.screenshot.ScreenshotService;
import org.lpw.ranch.push.helper.PushHelper;
import org.lpw.ranch.temporary.Temporary;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.office.pptx.PptxReader;
import org.lpw.tephra.pdf.PdfReader;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.Protocol;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Service(FileModel.NAME + ".service")
public class FileServiceImpl implements FileService, org.lpw.tephra.pdf.MediaWriter, org.lpw.tephra.office.MediaWriter {
    @Inject
    private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private DateTime dateTime;
    @Inject
    private Numeric numeric;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private PdfReader pdfReader;
    @Inject
    private PptxReader pptxReader;
    @Inject
    private Temporary temporary;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PushHelper pushHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private ElementService elementService;
    @Inject
    private ScreenshotService screenshotService;
    @Inject
    private DownloadService downloadService;
    @Inject
    private FileDao fileDao;

    @Override
    public JSONArray query(String editor) {
        return modelHelper.toJson(fileDao.query(editor).getList(), (file, object) -> {
            object.remove("id");
            object.remove("uri");
        });
    }

    @Override
    public Map<String, Integer> count() {
        return fileDao.count();
    }

    @Override
    public Set<String> editors() {
        return fileDao.editors();
    }

    @Override
    public void save(String editor, String type, File file) {
        FileModel model = fileDao.find(editor, type);
        if (model == null) {
            model = new FileModel();
            model.setEditor(editor);
            model.setType(type);
        }
        model.setUri(wormholeHelper.file(null, null, null, file));
        model.setSize(file.length());
        model.setTime(dateTime.now());
        fileDao.save(model);
    }

    @Override
    public JSONObject upload(UploadReader uploadReader) throws IOException {
        File file = new File(context.getAbsolutePath(temporary.save(uploadReader.getInputStream(),
                uploadReader.getFileName().substring(uploadReader.getFileName().lastIndexOf('.')).toLowerCase())));
        String type = uploadReader.getParameter("type");
        List<String> list = type.equals("pdf") ? pdfReader.pngs(new FileInputStream(file), this, false) :
                pptxReader.pngs(new FileInputStream(file), this, false);
        JSONObject object = new JSONObject();
        if (validator.isEmpty(list)) {
            io.delete(file);
            object.put("success", false);

            return object;
        }

        String editor = uploadReader.getParameter("editor");
        if (editor != null && editorService.findById(editor) == null)
            editor = null;
        if (editor == null) {
            EditorModel em = new EditorModel();
            em.setType(uploadReader.getParameter("etype"));
            em.setTemplate(numeric.toInt(uploadReader.getParameter("template")));
            em.setImage(list.get(0));
            em.setScreenshot(em.getImage());
            editor = editorService.save(em).getString("id");
        } else {
            EditorModel em = new EditorModel();
            em.setId(editor);
            em.setImage(list.get(0));
            em.setScreenshot(em.getImage());
            editorService.modify(em, -1);
        }

        FileModel model = fileDao.find(editor, type);
        if (model == null) {
            model = new FileModel();
            model.setEditor(editor);
            model.setType(type);
        }
        model.setUri(wormholeHelper.file(null, null, null, file));
        model.setSize(file.length());
        model.setTime(dateTime.now());
        fileDao.save(model);

        elementService.deletes(editor);
        screenshotService.delete(editor);
        for (int i = 0, size = list.size(); i < size; i++) {
            ElementModel element = new ElementModel();
            element.setEditor(editor);
            element.setParent(editor);
            element.setSort(i);
            screenshotService.create(editor, i, elementService.save(element).getString("id"), list.get(i));
        }
        io.delete(file);

        object.put("success", true);
        object.put("path", model.getUri());
        object.put("name", uploadReader.getName());
        object.put("fileName", uploadReader.getFileName());
        object.put("fileSize", model.getSize());

        return object;
    }

    @Override
    public String write(org.lpw.tephra.pdf.MediaType mediaType, String fileName, InputStream inputStream) {
        return wormholeHelper.image(null, null, mediaType.getSuffix(), null, inputStream);
    }

    @Override
    public String write(org.lpw.tephra.office.MediaType mediaType, String fileName, InputStream inputStream) {
        return wormholeHelper.image(null, null, mediaType.getSuffix(), null, inputStream);
    }

    @Override
    public String download(String editor, String type, String email) {
        boolean markable = !editorService.nomark(editor);
        if (markable)
            type += ".free";
        FileModel file = fileDao.find(editor, type);
        if (file == null)
            return null;

        String uri = wormholeHelper.temporary(file.getUri());
        if (validator.isEmpty(uri))
            return null;

        file.setDownload(file.getDownload() + 1);
        fileDao.save(file);
        downloadService.save(editor, type, file.getUri(), uri);

        String url = wormholeHelper.getUrl(Protocol.Https, uri, false);
        if (validator.isEmail(email)) {
            JSONObject args = new JSONObject();
            args.put("url", url);
            pushHelper.send(FileModel.NAME + ".download", userHelper.id(), email, args);
        }

        return url;
    }
}
