package com.payments.services;

import com.payments.beans.OrderInsightsRequest;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RequestBuilderService {

	private static final Random RANDOM = new Random();

	public String curlChargeBackHelpRequest(String txnAmount) {
		return "curl --location 'http://localhost:8080/ngp/chargebackhelp' \\\n"
				+ "--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NDQ3OTg2ODIsImV4cCI6MTc0NDgwMTY4MiwianRpIjoiODQzZTVhZDQtNjFmNC00OTI4LTljZjAtY2IzY2ZmMWI4MWIyIn0.9nqwhx6tw-DioyUC-noksnseUPUssSA3oPa6BGU4qJo' \\\n"
				+ "--header 'Content-Type: application/json' \\\n" + "--data '{\n" + "    \"entity\": \"alerts\",\n"
				+ "    \"event\": \"received\",\n" + "    \"data\": [\n" + "        {\n" + "            \"id\": \""
				+ RANDOM.nextInt(9999) + RANDOM.nextInt(9999) + "\",\n"
				+ "            \"merchant_id\": \"TES250325001\",\n" + "            \"merchant_name\": \"89\",\n"
				+ "            \"descriptor_id\": 111,\n" + "            \"descriptor_name\": \"test\",\n"
				+ "            \"source\": \"verifi\",\n" + "            \"type\": \"Dispute\",\n"
				+ "            \"external_id\": \"\",\n" + "            \"alert_timestamp\": \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC)
						.format(Instant.now().plusSeconds(30))
				+ "Z\",\n" + "            \"issuer\": null,\n" + "            \"card_bin\": \"420000\",\n"
				+ "            \"card_last4\": \"0000\",\n" + "            \"card_brand\": \"Visa\",\n"
				+ "            \"transaction_timestamp\": \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC).format(Instant.now())
				+ "Z\",\n" + "            \"amount\": \"" + txnAmount + "\",\n" + "            \"currency\": \"EUR\",\n"
				+ "            \"arn\": \"\",\n" + "            \"auth_code\": \"\",\n" + "            \"mcc\": 1,\n"
				+ "            \"main_descriptor_name\": \"valitorpayMidTest\",\n" + "            \"gateway\": null,\n"
				+ "            \"gateway_id\": null,\n" + "            \"gateway_refund_id\": null,\n"
				+ "            \"transaction_type\": null,\n" + "            \"consumer_name\": null,\n"
				+ "            \"status\": \"pending\",\n" + "            \"resolved_at\": null,\n"
				+ "            \"refunded\": null,\n" + "            \"message\": null,\n"
				+ "            \"external_source\": null,\n" + "            \"external_liability\": null,\n"
				+ "            \"external_merchant_id\": 89,\n" + "            \"credited_at\": null,\n"
				+ "            \"credit_outcome\": \"Not Requested\",\n" + "            \"credit_reason\": [],\n"
				+ "            \"credit_request_at\": null,\n" + "            \"chargeback_at\": null,\n"
				+ "            \"status_code\": null,\n" + "            \"resolved_by\": null,\n"
				+ "            \"overlap_id\": null,\n" + "            \"duplicate_id\": null,\n"
				+ "            \"outcome\": \"received\",\n" + "            \"created_at\":  \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC)
						.format(Instant.now().plusSeconds(30))
				+ "Z\",\n" + "            \"updated_at\":  \"" + DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
						.withZone(ZoneOffset.UTC).format(Instant.now().plusSeconds(30))
				+ "Z\"\n" + "        }\n" + "    ]\n" + "}'\n";
	}

	public static void main(String[] args) {
		RequestBuilderService builderService = new RequestBuilderService();
		String s = builderService.curlChargeBackHelpRequest("10.00");
		System.out.println(s);
	}

//    public String curlForOrderInsightsRequest() {
//		OrderInsightsRequest request = new OrderInsightsRequest();
//		request.setCaid();
//	}
}
