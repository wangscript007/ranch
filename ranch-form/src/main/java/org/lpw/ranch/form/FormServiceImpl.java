package org.lpw.ranch.form;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(FormModel.NAME + ".service")
public class FormServiceImpl implements FormService {
    @Inject
    private FormDao formDao;
}
