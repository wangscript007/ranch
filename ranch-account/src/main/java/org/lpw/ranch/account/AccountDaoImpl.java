package org.lpw.ranch.account;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(AccountModel.NAME + ".dao")
class AccountDaoImpl implements AccountDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AccountModel> query(String user, String owner, int type, int minBalance, int maxBalance, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_user", user);
        append(where, args, "c_owner", owner);
        append(where, args, "c_type", type, "=");
        append(where, args, "c_balance", minBalance, ">=");
        append(where, args, "c_balance", maxBalance, "<=");

        return liteOrm.query(new LiteQuery(AccountModel.class).where(where.toString()).order("c_user,c_owner,c_type").size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String column, String value) {
        if (value == null)
            return;

        if (!args.isEmpty())
            where.append(" and ");
        where.append(column).append("=?");
        args.add(value);
    }

    private void append(StringBuilder where, List<Object> args, String column, int value, String operation) {
        if (value < 0)
            return;

        if (!args.isEmpty())
            where.append(" and ");
        where.append(column).append(operation).append('?');
        args.add(value);
    }

    @Override
    public AccountModel findById(String id) {
        return liteOrm.findById(AccountModel.class, id);
    }

    @Override
    public AccountModel find(String user, String owner, int type) {
        return liteOrm.findOne(new LiteQuery(AccountModel.class).where("c_user=? and c_owner=? and c_type=?"), new Object[]{user, owner, type});
    }

    @Override
    public void save(AccountModel account) {
        liteOrm.save(account);
    }
}
