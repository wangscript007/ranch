package org.lpw.ranch.form;

/**
 * @author lpw
 */
interface FormDao {
    FormModel findById(String id);

    void save(FormModel form);
}
