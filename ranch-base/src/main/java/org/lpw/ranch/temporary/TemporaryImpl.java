package org.lpw.ranch.temporary;

import org.lpw.tephra.ctrl.upload.UploadService;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;

/**
 * @author lpw
 */
@Component("ranch.temporary")
public class TemporaryImpl implements Temporary, HourJob {
    @Inject
    private Io io;
    @Inject
    private Context context;
    @Inject
    private Generator generator;

    @Override
    public String root() {
        return UploadService.ROOT + "temp/";
    }

    @Override
    public String newSavePath(String suffix) {
        io.mkdirs(context.getAbsolutePath(root()));

        return root() + generator.random(32) + suffix;
    }

    @Override
    public String save(byte[] bytes, String suffix) {
        String path = newSavePath(suffix);
        io.write(context.getAbsolutePath(path), bytes);

        return path;
    }

    @Override
    public String save(InputStream inputStream, String suffix) {
        String path = newSavePath(suffix);
        io.write(context.getAbsolutePath(path), inputStream);

        return path;
    }

    @Override
    public void executeHourJob() {
        File[] files = new File(context.getAbsolutePath(root())).listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files)
            if (System.currentTimeMillis() - file.lastModified() > TimeUnit.Day.getTime())
                io.delete(file);
    }
}
