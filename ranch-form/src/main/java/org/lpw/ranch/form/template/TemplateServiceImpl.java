package org.lpw.ranch.form.template;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(TemplateModel.NAME + ".service")
public class TemplateServiceImpl implements TemplateService {
    @Inject
    private TemplateDao templateDao;
}
