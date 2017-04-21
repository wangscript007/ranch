package org.lpw.ranch.dbtool.table;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(TableModel.NAME + ".service")
public class TableServiceImpl implements TableService {
    @Inject
    private TableDao tableDao;
}
