package com.payments.commons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.gateway.payment.AddressDTO;
import com.gateway.payment.BillingDTO;

public class TestConstants {

	public static final String SUCCESS = "http://192.168.1.103/success.html";
	public static final String FAIL = "http://192.168.1.103/failed.html";
	public static final String CANCEL = "http://192.168.1.103/cancel.html";

	public static final String PAYMENT_ROUTING_ACCESS_TOKEN = "435df721059b45ca94985d6725eb63f1";
	public static final String TEST_WITH_ASHISH_ACCESS_TOKEN = "dc8c4655e89d44df936c7082f1fb4ab7";
	public static final String STREAMHUB_ACCESS_TOKEN = "cecf12cbd8c841798b16fd135cf5d7bb";
	public static final String LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN = "fd93bf472fea4a55ae029c5c407bacc4";
	public static final String WOOD_MERCHANT_ACCESS_TOKEN = "f9e596e8e8cf4233b266feb30a51e75e";
	public static final String QA_WOOD_MERCHANT_ACCESS_TOKEN = "e00438ae97194d76b5b7829d1bdc9199";
	public static final String QA_TEST_MERCHANT_ACCESS_TOKEN = "fbf47c31ebab42a3bd079f49709091db";
	public static final String QA_PAYALLY_ACCESS_TOKEN = "d4de46adcbe0469695d82db346f4df80";
	public static final String LOCAL_MERCHANT_ACCESS_TOKEN = "58ff8059ab734dcb881d4bef81f46362";
	public static final String TEST_ALGO_ACCESS_TOKEN = "3ca23dadca634170be596990d4db423c";
	public static final String QA_PRT_ACCESS_TOKEN = "07bd11e2892247c29b611ca731e22045";
	public static final String TEST_ALGO_GRANDCHILD_1_ACCESS_TOKEN = "4c5187534c28450aa5311a1a12bd0be6";
	public static final String TEST_ALGO_GRANDCHILD_3_ACCESS_TOKEN = "b50668e128c342f68f015f2213455b4d";
	public static final String BLUE_ORBIT_ACCESS_TOKEN = "48bc97c23ef14107b1e8cf599f1a5065";

	public static final String PAYMENT_ROUTING_MERCHANT_ID = "PAY230124001";
	public static final String TEST_WITH_ASHISH_MERCHANT_ID = "TES250325001";
	public static final String STREAMHUB_MERCHANT_ID = "SST030524001";
	public static final String TEST_ALGO_MERCHANT_ID = "ALG210425001";
	public static final String WOOD_MERCHANT_MERCHANT_ID = "GIT210317001";
	public static final String QA_WOOD_MERCHANT_MERCHANT_ID = "GIT210317001";
	public static final String QA_TEST_MERCHANT_MERCHANT_ID = "TEM011220001";
	public static final String QA_PAYALLY_MERCHANT_ID = "PTM141223001";
	public static final String LOCAL_MERCHANT_MERCHANT_ID = "LMO050925001";
	public static final String QA_PRT_MERCHANT_ID = "PRT171025001";
	public static final String TEST_ALGO_GRANDCHILD_1_MERCHANT_ID = "TAG1040625001";
	public static final String TEST_ALGO_GRANDCHILD_3_MERCHANT_ID = "TAG3040625001";
	public static final String BLUE_ORBIT_MERCHANT_ID = "BLU230926002";

	public static final String PAYMENT_ROUTING_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/PAY230124001-crt.pem";
	public static final String TEST_WITH_ASHISH_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/local/TES250325001-crt-CP-DEV.pem";
	public static final String TEST_ALGO_GRANDCHILD_1_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/TAG1040625001-crt-CP-LOCAL.pem";
	public static final String TEST_ALGO_GRANDCHILD_3_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/TAG3040625001-crt-CP-LOCAL.pem";
	public static final String STREAMHUB_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/SST030524001-crt-CP-QA.pem";
	public static final String TEST_ALGO_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/ALG210425001-crt-CP-DEV.pem";
	public static final String WOOD_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/GIT210317001-crt-dev.pem";
	public static final String QA_WOOD_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/dev/GIT210317001-crt.pem";
	public static final String QA_TEST_MERCHANT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/TEM011220001-crt-CP-QA.pem";
	public static final String QA_PAYALLY_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/stag/PTM141223001-crt-PY-QA.pem";
	public static final String QA_PRT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/stag/PRT171025001-crt-CP-QA.pem";
	public static final String BLUE_ORBIT_CERT_PATH = "/home/ashish/Certificates/merchant_certificates/BLU230926002-crt.pem";

	public static final String MASTERCARD = "5500000000000004";
	public static final String VISA_CARD = "4200000000000000";
	public static final String THREEDS_VISA_CARD = "4000000000001000";
	public static final String VISA_CARD_AMOUNT_BASED_FAILED = "4242000000000000";
	public static final String VISA_3D_SECURE_CARD = "4711100000000000";
	public static final String MASTER_3D_SECURE_CARD = "5299910010000015";

	public static AddressDTO address = new AddressDTO("Ravi", "Kumar", "BR", "31232567456",
			"203a4abe61b549c0ba7d62df014b9e25@gmail.com", "abc", "abc", "999999", "Brasilia", "MTA");
	public static final BillingDTO BILLING_ADDRESS = new BillingDTO(address);

	public static final String CARD_HOLDER_NAME = "Test User";

	public static List<String> SUCCESS_CARD = List.of(MASTERCARD, VISA_CARD, VISA_3D_SECURE_CARD,
			MASTER_3D_SECURE_CARD);

	public static List<String> MID_TAGS = List.of("ecommpay", "acquired", "credorax", "truevo", "payxpert", "payreto",
			"nmi");
	public static List<String> JSON_ACQUIRER = List.of("ecommpay", "acquired", "payxpert", "braintree", "nmi",
			"stripe");

	public static String getUniqueRef(String type) {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return type + timeStamp + new Random().nextInt(10);
	}

	public static String generateARN(int size) {
		StringBuilder sb = new StringBuilder(size);
		sb.append((int) (Math.random() * 9) + 1); // First digit 1-9
		for (int i = 0; i < size; i++) {
			sb.append((int) (Math.random() * 10)); // Remaining digits 0-9
		}
		return sb.toString();
	}

	public static String generateAuthCode() {
		return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}

}
