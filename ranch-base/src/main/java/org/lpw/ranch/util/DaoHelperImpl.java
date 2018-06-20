package org.lpw.ranch.util;

import org.lpw.tephra.dao.jdbc.DataSource;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author lpw
 */
@Repository("ranch.util.dao-helper")
public class DaoHelperImpl implements DaoHelper {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, String value) {
        where(where, args, column, operation, value, false);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, String value, boolean and) {
        if (validator.isEmpty(value))
            return;

        appendWhere(where, args, column, operation, value, and);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, long value) {
        where(where, args, column, operation, value, false);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, long value, boolean and) {
        if (value < 0L)
            return;

        appendWhere(where, args, column, operation, value, and);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Date value) {
        where(where, args, column, operation, value, false);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Date value, boolean and) {
        if (value == null)
            return;

        appendWhere(where, args, column, operation, value, and);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Timestamp value) {
        where(where, args, column, operation, value, false);
    }

    @Override
    public void where(StringBuilder where, List<Object> args, String column, DaoOperation operation, Timestamp value, boolean and) {
        if (value == null)
            return;

        appendWhere(where, args, column, operation, value, and);
    }

    @Override
    public void in(StringBuilder where, List<Object> args, String column, Set<?> values) {
        in(where, args, column, values, false);
    }

    @Override
    public void in(StringBuilder where, List<Object> args, String column, Set<?> values, boolean and) {
        if (validator.isEmpty(values))
            return;

        if (values.size() == 1) {
            appendWhere(where, args, column, DaoOperation.Equals, values.iterator().next(), and);

            return;
        }

        appendColumn(where, args, column, and);
        where.append(" IN(");
        boolean has = false;
        for (Object value : values) {
            if (has)
                where.append(',');
            where.append('?');
            args.add(value);
            has = true;
        }
        where.append(')');
    }

    @Override
    public void like(String dataSource, StringBuilder where, List<Object> args, String column, String value) {
        like(dataSource, where, args, column, value, true, true, false);
    }

    @Override
    public void like(String dataSource, StringBuilder where, List<Object> args, String column, String value, boolean and) {
        like(dataSource, where, args, column, value, true, true, and);
    }

    @Override
    public void like(String dataSource, StringBuilder where, List<Object> args, String column, String value, boolean prefix, boolean suffix) {
        like(dataSource, where, args, column, value, prefix, suffix, false);
    }

    @Override
    public void like(String dataSource, StringBuilder where, List<Object> args, String column, String value,
                     boolean prefix, boolean suffix, boolean and) {
        if (validator.isEmpty(value))
            return;

        if (!prefix && !suffix) {
            appendWhere(where, args, column, DaoOperation.Equals, value, and);

            return;
        }

        appendColumn(where, args, column, and);
        where.append(" LIKE ?");
        args.add(this.dataSource.getDialect(dataSource).getLike(value, prefix, suffix));
    }

    private void appendWhere(StringBuilder where, List<Object> args, String column, DaoOperation operation, Object value, boolean and) {
        appendColumn(where, args, column, and);
        where.append(operation.get());
        args.add(value);
    }

    private void appendColumn(StringBuilder where, List<Object> args, String column, boolean and) {
        if (and || !args.isEmpty())
            where.append(" AND ");
        where.append(column);
    }
}
