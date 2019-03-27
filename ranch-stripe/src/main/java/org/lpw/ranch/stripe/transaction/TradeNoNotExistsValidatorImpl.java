package org.lpw.ranch.stripe.transaction;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TransactionService.VALIDATOR_TRADE_NO_NOT_EXISTS)
public class TradeNoNotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private TransactionDao transactionDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return transactionDao.count(parameter) == 0;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return TransactionModel.NAME + ".trade-no.exists";
    }
}
