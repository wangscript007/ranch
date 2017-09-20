package org.lpw.ranch.chrome;

import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ChromeModel.NAME + ".chromes")
public class ChromesImpl implements Chromes, ContextRefreshedListener {
    private Map<Chrome.Type, Chrome> map;

    @Override
    public Chrome get(Chrome.Type type) {
        return map.get(type);
    }

    @Override
    public int getContextRefreshedSort() {
        return 15;
    }

    @Override
    public void onContextRefreshed() {
        map = new HashMap<>();
        BeanFactory.getBeans(Chrome.class).forEach(chrome -> map.put(chrome.getType(), chrome));
    }
}
