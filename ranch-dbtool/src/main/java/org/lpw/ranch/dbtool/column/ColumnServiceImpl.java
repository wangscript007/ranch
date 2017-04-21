package org.lpw.ranch.dbtool.column;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ColumnModel.NAME + ".service")
public class ColumnServiceImpl implements ColumnService {
    @Inject
    private ColumnDao columnDao;
}
