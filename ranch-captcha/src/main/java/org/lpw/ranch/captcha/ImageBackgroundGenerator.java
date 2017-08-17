package org.lpw.ranch.captcha;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import org.lpw.tephra.util.Generator;

import java.awt.image.BufferedImage;

/**
 * @author lpw
 */
public class ImageBackgroundGenerator implements BackgroundGenerator {
    private Generator generator;
    private int width;
    private int height;
    private BufferedImage[] images;

    public ImageBackgroundGenerator(Generator generator, int width, int height, BufferedImage[] images) {
        this.generator = generator;
        this.width = width;
        this.height = height;
        this.images = images;
    }

    @Override
    public int getImageHeight() {
        return height;
    }

    @Override
    public int getImageWidth() {
        return width;
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = images[generator.random(0, images.length - 1)];
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int x = generator.random(0, image.getWidth() - width);
        int y = generator.random(0, image.getHeight() - height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bufferedImage.setRGB(i, j, image.getRGB(x + i, y + j));

        return bufferedImage;
    }
}
