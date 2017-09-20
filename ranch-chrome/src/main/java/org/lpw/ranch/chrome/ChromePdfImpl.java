package org.lpw.ranch.chrome;

import io.webfolder.cdp.session.Session;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(ChromeModel.NAME + ".pdf")
public class ChromePdfImpl extends ChromeSupport {
    private static final double INCHES = 96.0D;

    @Override
    public Type getType() {
        return Type.Pdf;
    }

    @Override
    byte[] execute(ChromeModel chrome, Session session) {
        return session.getCommand().getPage().printToPDF(
                false, false, true, 1.0D,
                chrome.getWidth() / INCHES, chrome.getHeight() / INCHES,
                0.0D, 0.0D, 0.0D, 0.0D,
                chrome.getPages(), true);
    }
}
