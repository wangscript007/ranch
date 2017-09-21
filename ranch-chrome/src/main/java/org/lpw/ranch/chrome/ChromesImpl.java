package org.lpw.ranch.chrome;

import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.storage.StorageListener;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Service(ChromeModel.NAME + ".chromes")
public class ChromesImpl implements Chromes, ContextRefreshedListener, StorageListener {
    @Inject
    private Io io;
    @Inject
    private Converter converter;
    @Inject
    private Numeric numeric;
    @Inject
    private Generator generator;
    @Inject
    private Logger logger;
    @Value("${ranch.chrome.host-port:/WEB-INF/chrome}")
    private String hps;
    private String[] hosts;
    private int[] ports;

    private Map<Chrome.Type, Chrome> map;

    @Override
    public Chrome get(Chrome.Type type) {
        int i = generator.random(0, hosts.length - 1);

        return map.get(type).set(hosts[i], ports[i]);
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

    @Override
    public String getStorageType() {
        return Storages.TYPE_DISK;
    }

    @Override
    public String[] getScanPathes() {
        return new String[]{hps};
    }

    @Override
    public void onStorageChanged(String path, String absolutePath) {
        Set<String> set = new HashSet<>();
        for (String string : converter.toArray(io.readAsString(absolutePath), "\n")) {
            string = string.trim();
            if (string.equals("") || string.startsWith("#") || string.indexOf(':') == -1)
                continue;

            set.add(string);
        }
        hosts = new String[set.size()];
        ports = new int[hosts.length];
        int i = 0;
        for (String hp : set) {
            String[] array = hp.split(":");
            hosts[i] = array[0];
            ports[i++] = numeric.toInt(array[1]);
        }

        if (logger.isInfoEnable())
            logger.info("更新Chrome调试服务器端口号集[{}]。", converter.toString(set));
    }
}
