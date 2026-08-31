package com.payments.beans;

import lombok.Data;

@Data
public class SubscriptionRequest {

    String planId;
    String subscriptionId = "";
    boolean staging;
}
