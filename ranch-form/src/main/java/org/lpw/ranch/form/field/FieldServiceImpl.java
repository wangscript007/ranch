package org.lpw.ranch.form.field;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(FieldModel.NAME + ".service")
public class FieldServiceImpl implements FieldService {
    @Inject
    private FieldDao fieldDao;
}
