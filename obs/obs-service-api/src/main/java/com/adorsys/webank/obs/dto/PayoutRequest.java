package com.adorsys.webank.obs.dto;

public class PayoutRequest {
        private String accountID;
        private String amount;
        private String accountIBAN;
        private String otheraccountIBAN;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountIBAN() {
        return accountIBAN;
    }

    public void setAccountIBAN(String accountIBAN) {
        this.accountIBAN = accountIBAN;
    }

    public String getOtheraccountIBAN() {
        return otheraccountIBAN;
    }

    public void setOtheraccountIBAN(String otheraccountIBAN) {
        this.otheraccountIBAN = otheraccountIBAN;
    }

    // Getters and Setters
        public String getAccountID() {
            return accountID;
        }

        public void setAccountID(String accountID) {
            this.accountID = accountID;
        }
}
