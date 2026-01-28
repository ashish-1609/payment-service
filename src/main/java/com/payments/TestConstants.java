package com.payments;

import com.gateway.payment.AddressDTO;
import com.gateway.payment.BillingDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class TestConstants {

    public static final String SUCCESS = "http://192.168.1.49/success.html";
    public static final String FAIL = "http://192.168.1.49/failed.html";
    public static final String CANCEL = "http://192.168.1.49/cancel.html";

    public static final String PAYMENT_ROUTING_ACCESS_TOKEN = "435df721059b45ca94985d6725eb63f1";
    public static final String TEST_WITH_ASHISH_ACCESS_TOKEN = "dc8c4655e89d44df936c7082f1fb4ab7";
    public static final String LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN = "1b5cc0ffa58946adbbc0bcbbe49700d4";
    public static final String WOOD_MERCHANT_ACCESS_TOKEN = "da255112877f4f88ad50ef6ced31ac61";
    public static final String QA_WOOD_MERCHANT_ACCESS_TOKEN = "e00438ae97194d76b5b7829d1bdc9199";
    public static final String QA_TEST_MERCHANT_ACCESS_TOKEN = "de592c848d3a4a6c89df49009b56fd66";
    public static final String QA_PAYALLY_ACCESS_TOKEN = "d4de46adcbe0469695d82db346f4df80";
    public static final String LOCAL_MERCHANT_ACCESS_TOKEN = "58ff8059ab734dcb881d4bef81f46362";
    public static final String TEST_ALGO_ACCESS_TOKEN = "3ca23dadca634170be596990d4db423c";
    public static final String QA_PRT_ACCESS_TOKEN = "07bd11e2892247c29b611ca731e22045";

    public static final String PAYMENT_ROUTING_MERCHANT_ID = "PAY230124001";
    public static final String TEST_WITH_ASHISH_MERCHANT_ID = "TES250325001";
    public static final String TEST_ALGO_MERCHANT_ID = "ALG210425001";
    public static final String WOOD_MERCHANT_MERCHANT_ID = "GIT210317001";
    public static final String QA_WOOD_MERCHANT_MERCHANT_ID = "GIT210317001";
    public static final String QA_TEST_MERCHANT_MERCHANT_ID = "TEM011220001";
    public static final String QA_PAYALLY_MERCHANT_ID = "PTM141223001";
    public static final String LOCAL_MERCHANT_MERCHANT_ID = "LMO050925001";
    public static final String QA_PRT_MERCHANT_ID = "PRT171025001";

    public static final String PAYMENT_ROUTING_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/PAY230124001-crt.pem";
    public static final String TEST_WITH_ASHISH_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/local/TES250325001-crt-CP-DEV.pem";
    public static final String TEST_ALGO_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/ALG210425001-crt-CP-DEV.pem";
    public static final String WOOD_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/GIT210317001-crt.pem";
    public static final String QA_WOOD_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/GIT210317001-crt.pem";
    public static final String QA_TEST_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/stag/TEM011220001-crt-CP-QA.pem";
    public static final String QA_PAYALLY_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/stag/PTM141223001-crt-PY-QA.pem";
    public static final String QA_PRT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/stag/PRT171025001-crt-CP-QA.pem";

    public static final String MASTERCARD = "5500000000000004";
    public static final String VISA_CARD = "4200000000000000";
    public static final String VISA_CARD_AMOUNT_BASED_FAILED = "4242000000000000";
    public static final String VISA_3D_SECURE_CARD = "4711100000000000";
    public static final String MASTER_3D_SECURE_CARD = "5299910010000015";

    public static AddressDTO address = new AddressDTO("Ravi", "Kumar", "BR", "31232567456",
            "203a4abe61b549c0ba7d62df014b9e25@gmail.com",
            "abc", "abc", "999999", "Brasilia", "MTA");
    public static final BillingDTO BILLING_ADDRESS = new BillingDTO(address);

    public static final String CARD_HOLDER_NAME = "Test User";

    public static String getUniqueRef(String type) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return type + timeStamp + new Random().nextInt(10);
    }

    public static String generateARN() {
        StringBuilder sb = new StringBuilder(23);
        sb.append((int)(Math.random() * 9) + 1); // First digit 1-9
        for (int i = 0; i < 22; i++) {
            sb.append((int)(Math.random() * 10)); // Remaining digits 0-9
        }
        return sb.toString();
    }

    public static String generateAuthCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

}
