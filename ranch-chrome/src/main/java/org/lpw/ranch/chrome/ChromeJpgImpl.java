package org.lpw.ranch.chrome;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
