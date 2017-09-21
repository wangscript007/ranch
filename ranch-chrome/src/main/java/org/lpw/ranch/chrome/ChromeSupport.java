package org.lpw.ranch.chrome;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

/**
 * @author lpw
 */
public abstract class ChromeSupport implements Chrome {
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
