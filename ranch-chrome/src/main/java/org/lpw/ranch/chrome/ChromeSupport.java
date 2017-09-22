package org.lpw.ranch.chrome;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import org.lpw.tephra.util.Logger;

import javax.inject.Inject;

/**
 * @author lpw
 */
public abstract class ChromeSupport implements Chrome {
    @Inject
    protected Logger logger;
    private String host;
    private int port;

    @Override
    public Chrome set(String host, int port) {
        this.host = host;
        this.port = port;

        return this;
    }

    @Override
    public byte[] execute(ChromeModel chrome, String url) {
        if (logger.isDebugEnable())
            logger.debug("使用远程Chrome[{}:{}]服务导出[{}:{}]。", host, port, getType().name(), url);

        SessionFactory sessionFactory = new SessionFactory(host, port);
        Launcher launcher = new Launcher(sessionFactory);
        launcher.launch();
        Session session = sessionFactory.create(sessionFactory.createBrowserContext()).navigate(url).waitDocumentReady().wait(chrome.getWait() * 1000);
        byte[] bytes = execute(chrome, session);
        session.close();
        sessionFactory.close();

        return bytes;
    }

    abstract byte[] execute(ChromeModel chrome, Session session);
}
