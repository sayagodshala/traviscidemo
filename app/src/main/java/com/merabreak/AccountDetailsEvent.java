package com.merabreak;

import com.merabreak.models.AccountDetails;

/**
 * Created by sayagodshala on 24/05/16.
 */
public class AccountDetailsEvent {

    private AccountDetails accountDetails;

    public AccountDetailsEvent(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public AccountDetailsEvent() {
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }
}
