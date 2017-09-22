package org.lpw.ranch.chrome;

import io.webfolder.cdp.session.Session;
import org.lpw.tephra.util.Logger;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lpw
 */
public abstract class ChromeImgSupport extends ChromeSupport {
    @Override
    byte[] execute(ChromeModel chrome, Session session) {
        try {
            return subImage(session.captureScreenshot(), chrome.getX(), chrome.getY(), chrome.getWidth(), chrome.getHeight());
        } catch (IOException e) {
            logger.warn(e, "获取截屏图片时发生异常！");

            return null;
        }
    }

    private byte[] subImage(byte[] bytes, int x, int y, int width, int height) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();

        if (width <= 0)
            width = image.getWidth();
        else
            width = Math.min(width, image.getWidth());
        if (height <= 0)
            height = image.getHeight();
        else
            height = Math.min(height, image.getHeight());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(format(image.getSubimage(x, y, width, height)), formatName(), outputStream);
        outputStream.close();

        return outputStream.toByteArray();
    }

    abstract BufferedImage format(BufferedImage subImage);

    abstract String formatName();
}
