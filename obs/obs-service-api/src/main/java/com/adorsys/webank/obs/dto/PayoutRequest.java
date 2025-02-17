package com.adorsys.webank.obs.dto;

public class PayoutRequest {
        private String accountID;
        private String otherAccountID;
        private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // Getters and Setters
        public String getAccountID() {
            return accountID;
        }

        public void setAccountID(String accountID) {
            this.accountID = accountID;
        }

        public String getOtherAccountID() {
            return otherAccountID;
        }

        public void setOtherAccountID(String otherAccountID) {
            this.otherAccountID = otherAccountID;
        }
}
