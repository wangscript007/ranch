package org.lpw.ranch.classify;

import org.junit.Test;
import org.lpw.ranch.util.ServiceHelperTester;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class ClassifyHelperTest extends TephraTestSupport {
    @Inject
    private ServiceHelperTester serviceHelperTester;
    @Inject
    private ClassifyHelper classifyHelper;

    @Test
    public void get() {
        serviceHelperTester.get((ClassifyHelperImpl) classifyHelper, "ranch.classify");
    }

    @Test
    public void fill() {
        serviceHelperTester.fill((ClassifyHelperImpl) classifyHelper, "ranch.classify");
    }

}
