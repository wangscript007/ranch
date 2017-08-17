package org.lpw.ranch.captcha;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author lpw
 */
@Service(CaptchaModel.NAME + ".engine-factory")
public class EngineFactoryImpl implements EngineFactory {
    @Inject
    private Context context;
    @Inject
    private Validator validator;
    @Inject
    private Generator generator;
    @Inject
    private Logger logger;
    @Value("${ranch.captcha.font:/WEB-INF/captcha/font}")
    private String font;
    @Value("${ranch.captcha.font:/WEB-INF/captcha/background}")
    private String background;
    private Font[] fonts;
    private BufferedImage[] images;

    @Override
    public CaptchaEngine createCaptchaEngine(CaptchaModel captcha) {
        return new ListImageCaptchaEngine() {
            @Override
            protected void buildInitialFactories() {
                FontGenerator fontGenerator = new RandomFontGenerator(captcha.getFontMin(), captcha.getFontMax(), getFonts());
                BackgroundGenerator backgroundGenerator = createBackgroundGenerator(captcha.getWidth(), captcha.getHeight(), captcha.getBackground());
                TextPaster textPaster = new RandomTextPaster(captcha.getLength(), captcha.getLength(),
                        new RandomRangeColorGenerator(new int[]{0, 100}, new int[]{0, 100}, new int[]{0, 100}), true);
                addFactory(new GimpyFactory(new RandomWordGenerator(captcha.getChars()),
                        new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster)));
            }
        };
    }

    private Font[] getFonts() {
        if (fonts != null)
            return fonts;

        File[] files = new File(context.getAbsolutePath(font)).listFiles();
        if (files == null)
            return fonts = new Font[0];

        fonts = new Font[files.length];
        for (int i = 0; i < files.length; i++)
            fonts[i] = createFont(files[i]);

        return fonts;
    }

    private Font createFont(File file) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException | IOException e) {
            logger.warn(e, "读取字体文件[{}]时发生异常！", file);

            return null;
        }
    }

    private BackgroundGenerator createBackgroundGenerator(int width, int height, int background) {
        if (background != 1 || getImages() == null)
            return new FunkyBackgroundGenerator(width, height);

        return new ImageBackgroundGenerator(generator, width, height, images);
    }

    private BufferedImage[] getImages() {
        if (images != null)
            return images;

        File[] files = new File(context.getAbsolutePath(background)).listFiles();
        if (files == null)
            return null;

        images = new BufferedImage[files.length];
        try {
            for (int i = 0; i < files.length; i++)
                images[i] = ImageIO.read(files[i]);
        } catch (IOException e) {
            logger.warn(e, "读取验证码背景图片[{}]时发生异常！", background);
        }

        return images;
    }
}
