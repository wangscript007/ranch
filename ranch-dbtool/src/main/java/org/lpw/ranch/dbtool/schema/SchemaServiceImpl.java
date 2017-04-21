package org.lpw.ranch.dbtool.schema;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(SchemaModel.NAME + ".service")
public class SchemaServiceImpl implements SchemaService {
    @Inject
    private SchemaDao schemaDao;
}
