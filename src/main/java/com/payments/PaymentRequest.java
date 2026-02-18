package com.payments;

import lombok.Data;

@Data
public class PaymentRequest {

    private String acquirer;
    private String merchant;
    private String environment;
    private Integer numberOfTransactions;

    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;

    private boolean reconciliation;
    private boolean subscription;
    private boolean staging;
    private boolean captureTxn;
}
