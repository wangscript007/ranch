package org.lpw.ranch.chrome;

import io.webfolder.cdp.session.Session;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lpw
 */
@Service(ChromeModel.NAME + ".jpg")
public class ChromeJpgImpl extends ChromeImgSupport {
    @Override
    public Type getType() {
        return Type.Jpg;
    }

    @Override
    BufferedImage format(BufferedImage subImage) {
        BufferedImage jpegImage = new BufferedImage(subImage.getWidth(), subImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpegImage.createGraphics().drawImage(subImage, 0, 0, Color.WHITE, null);

        return jpegImage;
    }

    @Override
    String formatName() {
        return "JPEG";
    }
}
