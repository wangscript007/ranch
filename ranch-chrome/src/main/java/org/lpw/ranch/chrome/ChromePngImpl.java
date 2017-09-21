package org.lpw.ranch.chrome;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * @author lpw
 */

@Service(ChromeModel.NAME + ".png")
public class ChromePngImpl extends ChromeImgSupport {

    @Override
    public Type getType() {
        return Type.Png;
    }

    @Override
    BufferedImage format(BufferedImage subImage) {
        return subImage;
    }

    @Override
    String formatName() {
        return "PNG";
    }
}
