package com.payments;

import com.gateway.constants.enums.PaymentModeEnum;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum PaymentOption {

    // Argentina
    PAGO_FACIL_ARS("AR", "Argentina", "pago-facil-ars", "ARS", PaymentModeEnum.PAGO_FACIL, 500, 2000),
    RAPI_PAGO_ARS("AR", "Argentina", "rapi-pago-ars", "ARS", PaymentModeEnum.RAPI_PAGO, 500, 2000),
    BANK_TRANSFER_ARS("AR", "Argentina", "bank-transfer-ars", "ARS", PaymentModeEnum.BANK_TRANSFER, 500, 1000),
    MERCADO_PAGO_ARS("AR", "Argentina", "mercado-pago-ars", "ARS", PaymentModeEnum.MERCADO_PAGO, 100, 5000),

    // Brazil
    PIX_BRL("BR", "Brazil", "pix-brl", "BRL", PaymentModeEnum.PIX, 5, 5000),
    BOLETO_BRL("BR", "Brazil", "boleto-brl", "BRL", PaymentModeEnum.BOLETO, 10, 500),
    PICPAY_BRL("BR", "Brazil", "picpay-brl", "BRL", PaymentModeEnum.PICPAY, 5, 8000),

    // Chile
    BANK_TRANSFER_CLP("CL", "Chile", "bank-transfer-clp", "CLP", PaymentModeEnum.BANK_TRANSFER, 1000, 5000),
    EXPRESS_LIDER_CLP("CL", "Chile", "express-lider-clp", "CLP", PaymentModeEnum.EXPRESS_LIDER, 1000, 3000),
    LIDER_CLP("CL", "Chile", "lider-clp", "CLP", PaymentModeEnum.LIDER, 1000, 300000),
    MACH_CLP("CL", "Chile", "mach-clp", "CLP", PaymentModeEnum.MACH, 1000, 250000),

    // Colombia
    BANK_TRANSFER_COP("CO", "Colombia", "bank-transfer-cop", "COP", PaymentModeEnum.BANK_TRANSFER, 5000, 9000),
    EFECTY_COP("CO", "Colombia", "efecty-cop", "COP", PaymentModeEnum.EFECTY, 10000, 30000),
    PSE_COP("CO", "Colombia", "pse-cop", "COP", PaymentModeEnum.PSE, 5000, 40000),

    // Costa Rica
    PAYSER_CRC("CR", "Costa Rica", "payser-crc", "CRC", PaymentModeEnum.PAYSER, 1000, 5000),
    BANK_TRANSFER_CRC("CR", "Costa Rica", "bank-transfer-crc", "CRC", PaymentModeEnum.BANK_TRANSFER, 1000, 9000),
    PUNTOS_CRC("CR", "Costa Rica", "puntos-hey-crc", "CRC", PaymentModeEnum.PUNTOS_HEY, 500, 5000),

    // Ecuador
    BANK_TRANSFER_USD_EC("EC", "Ecuador", "bank_transfer-usd", "USD", PaymentModeEnum.BANK_TRANSFER, 5, 5000),

    // Mexico
    BANK_TRANSFER_MXN("MX", "Mexico", "bank-transfer-mxn", "MXN", PaymentModeEnum.BANK_TRANSFER, 50, 2000),
    SPEI_MXN("MX", "Mexico", "pse-cop", "MXN", PaymentModeEnum.SPEI, 50, 5000),
    OXXO_MXN("MX", "Mexico", "oxxo-mxn", "MXN", PaymentModeEnum.OXXO, 10, 5000),

    // Peru
    CASH_PEN("PE", "Peru", "cash-pen", "PEN", PaymentModeEnum.CASH, 5, 5000),
    BANK_TRANSFER_PEN("PE", "Peru", "bank-transfer-pen", "PEN", PaymentModeEnum.BANK_TRANSFER, 5, 2000),
    KASHIO_PEN("PE", "Peru", "kashio-pen", "PEN", PaymentModeEnum.KASHIO, 5, 8000),
    YAPE_PEN("PE", "Peru", "Yape-pen", "PEN", PaymentModeEnum.YAPE, 1, 3000),
    PLIN_PEN("PE", "Peru", "plin-pen", "PEN", PaymentModeEnum.PLIN, 1, 3000);

    private final String countryCode;
    private final String country;
    private final String midTag;
    private final String currency;
    private final PaymentModeEnum paymentMode;
    private final int minAmount;
    private final int maxAmount;

    PaymentOption(String countryCode, String country, String midTag, String currency,
                  PaymentModeEnum paymentMode, int minAmount, int maxAmount) {
        this.countryCode = countryCode;
        this.country = country;
        this.midTag = midTag;
        this.currency = currency;
        this.paymentMode = paymentMode;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getCountryCode() { return countryCode; }
    public String getCountry() { return country; }
    public String getMidTag() { return midTag; }
    public String getCurrency() { return currency; }
    public PaymentModeEnum getPaymentMode() { return paymentMode; }
    public int getMinAmount() { return minAmount; }
    public int getMaxAmount() { return maxAmount; }

    private static final List<PaymentOption> VALUES = Arrays.asList(values());
    private static final Random RANDOM = new Random();

    public static PaymentOption getRandomOption() {
        return VALUES.get(RANDOM.nextInt(VALUES.size()));
    }

    public BigDecimal getRandomAmount() {
        int amount = RANDOM.nextInt(maxAmount - minAmount + 1) + minAmount;
        return new BigDecimal(amount);
    }

    public static PaymentOption getByPaymentModeAndCurrency(String mode, String currency) {
        return VALUES.stream()
                .filter(option -> option.getPaymentMode().getValue().equals(mode)
                        && option.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }
}
