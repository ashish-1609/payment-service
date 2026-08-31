package com.payments.beans;

import lombok.Data;

@Data
public class PaymentRequest {

	private String acquirer;
	private String merchant;
	private String environment;
	private Integer count;

	private String cardNumber;
	private String expiryMonth;
	private String expiryYear;
	private String cvv;

	private boolean reconciliation;
	private String reconciliationType;

	private boolean subscription;
	private boolean staging;
	private boolean captureTxn;

	private boolean orderInsightsRequest;
	private SubscriptionRequest subscriptionRequest;

	private String accessToken;
	private String merchantId;
	private String certPath;
}
