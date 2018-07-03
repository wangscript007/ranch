package org.lpw.ranch.access;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.Interceptor;
import org.lpw.tephra.ctrl.Invocation;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.storage.StorageListener;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Controller(AccessModel.NAME + ".interceptor")
public class InterceptorImpl implements Interceptor, StorageListener {
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccessService accessService;
    @Value("${" + AccessModel.NAME + ".uri:/WEB-INF/access-uri}")
    private String uri;
    private Set<String> uris = Collections.synchronizedSet(new HashSet<>());
    private Set<String> regexes = Collections.synchronizedSet(new HashSet<>());

    @Override
    public int getSort() {
        return 34;
    }

    @Override
    public Object execute(Invocation invocation) throws Exception {
        String uri = request.getUri();
        if (contains(uri)) {
            Map<String, String> map = new HashMap<>(header.getMap());
            accessService.save(map.remove("host"), uri, header.getIp(),
                    map.remove("user-agent"), map.remove("referer"), map);
        }

        return invocation.invoke();
    }

    private boolean contains(String uri) {
        if (userHelper.signUri().equals(uri))
            return false;

        if (uris.contains(uri))
            return true;

        for (String regex : regexes)
            if (validator.isMatchRegex(regex, uri))
                return true;

        return false;
    }

    @Override
    public String getStorageType() {
        return Storages.TYPE_DISK;
    }

    @Override
    public String[] getScanPathes() {
        return new String[]{uri};
    }

    @Override
    public void onStorageChanged(String path, String absolutePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
            Set<String> uris = Collections.synchronizedSet(new HashSet<>());
            Set<String> regexes = Collections.synchronizedSet(new HashSet<>());
            for (String line; (line = reader.readLine()) != null; ) {
                line = line.trim();
                if (line.equals("") || line.charAt(0) == '#')
                    continue;

                if (line.charAt(0) == ':')
                    regexes.add(line.substring(1));
                else
                    uris.add(line);
            }
            this.uris = uris;
            this.regexes = regexes;
            if (logger.isInfoEnable())
                logger.info("更新访问日志URI配置[{}:{}]。", uris.toString(), regexes.toString());
        } catch (Throwable throwable) {
            logger.warn(throwable, "读取请求日志uri配置[{}]时发生异常！", path);
        }
    }
}
