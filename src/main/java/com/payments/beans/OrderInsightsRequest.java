package com.payments.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class OrderInsightsRequest {

    private BigDecimal amount;
    private String authCode;
    private int cardBin;
    private String merchantId;
    private String externalMerchantId;
    private String caid;
    private String currency;
    private String source;
    private String descriptor;
    private String arn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime transactionDate;
    private int cardLast4;

}
