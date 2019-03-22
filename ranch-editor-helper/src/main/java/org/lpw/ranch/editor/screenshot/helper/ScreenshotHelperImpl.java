package org.lpw.ranch.editor.screenshot.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Controller("ranch.editor.screenshot.helper")
public class ScreenshotHelperImpl implements ScreenshotHelper {
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private Carousel carousel;
    @Value("${ranch.editor.screenshot.key:ranch.editor.screenshot}")
    private String key;
    private String queryKey;

    @Override
    public JSONArray query(String editor) {
        if (queryKey == null)
            queryKey = key + ".query";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("editor", editor);

        return carousel.service(queryKey, null, parameter, false, JSONArray.class);
    }
}
