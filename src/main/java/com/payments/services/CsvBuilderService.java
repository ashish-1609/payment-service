package com.payments.services;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.gateway.constants.enums.CardTypeEnum;
import com.payments.beans.PaymentRequest;
import com.payments.commons.TestConstants;
import com.payments.commons.Utils;
import com.payments.enums.ChargebackReason;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CsvBuilderService {

	public CsvBuilderService() {
	}

	public void prepareReconciliationFile(List<String> items, PaymentRequest paymentRequest, String amount, String ref,
			String cardType) {
		Random random = new Random();
		String chargebackReason = cardType.equalsIgnoreCase(CardTypeEnum.VISA_CARD.getValue())
				? ChargebackReason.VISA_REASONS.get(random.nextInt(ChargebackReason.VISA_REASONS.size())).getCode()
				: ChargebackReason.MASTERCARD_REASONS.get(random.nextInt(ChargebackReason.MASTERCARD_REASONS.size()))
						.getCode();
		updateFileData(items, paymentRequest, amount, ref, chargebackReason);
	}

	private void updateFileData(List<String> items, PaymentRequest paymentRequest, String amount, String ref,
			String chargebackReason) {
		log.info("item to be updated in files: \n{}", items);
		LocalDateTime date = LocalDateTime.now();
		String acquirer = paymentRequest.getAcquirer().toLowerCase();
		String reconciliationType = paymentRequest.getReconciliationType().toLowerCase();
		if (acquirer.equalsIgnoreCase("acquired") && reconciliationType.equalsIgnoreCase("capture")) {
			// ToDo
		} else if (acquirer.equalsIgnoreCase("truevo") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForTruevoReconciliationData(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("fraud")) {
			updateForEcommpayFraudData(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("payment")) {
			updateForEcommpayReconciliationData(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("chargeback")) {
			updateForEcommpayChargeback(items, amount, ref, date, chargebackReason);
		} else if (acquirer.equalsIgnoreCase("credorax") && reconciliationType.equalsIgnoreCase("fraud")) {
			updateForCredoraxFraudData(items, amount, ref, date, chargebackReason);
		} else if (acquirer.equalsIgnoreCase("credorax") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForCredoraxReconciliationData(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("payment")) {
			updateForPayxpertReconciliation(items, amount, ref, date, "capture");
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForPayxpertReconciliation(items, amount, ref, date, "rebill");
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("chargeback")) {
			updateForPayxpertChargeback(items, amount, ref, date, chargebackReason);
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("fraud")) {
			updateForPayxpertFraud(items, amount, ref, date, chargebackReason);
		} else if (acquirer.equalsIgnoreCase("payreto") && reconciliationType.equalsIgnoreCase("payment")) {
			updateForPayretoReconciliaition(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("rapyd") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForValitorpay(items, amount, ref, date);
		} else if (acquirer.equalsIgnoreCase("braintree") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForBraintreeReconciliation(items, amount, ref, date, paymentRequest.isCaptureTxn());
		} else if (acquirer.equalsIgnoreCase("nmi") && reconciliationType.equalsIgnoreCase("capture")) {
			updateForNmiReconciliation(paymentRequest.isCaptureTxn(), ref, date, amount, items);
		} else if (acquirer.equalsIgnoreCase("stripe") && reconciliationType.equalsIgnoreCase("chargeback")) {
			updateForStripeChargeback(items, amount, ref, date, chargebackReason);
		}
	}

	private void updateForStripeChargeback(List<String> items, String amount, String ref, LocalDateTime date,
			String chargebackReason) {
		long epochSecond = date.plusMinutes(2).atZone(ZoneId.systemDefault()).toEpochSecond();
		if (items.isEmpty()) {
			items.add("[");
		}
		String chb = TestConstants.getUniqueRef("CHB");
		JSONObject dispute = new JSONObject();
		dispute.put("balance_transaction", "txn_1UHrGSDwr7i4Cg9Vp2UljJMc");
		dispute.put("reason", "fraudulent");
		dispute.put("amount", Math.round(Double.parseDouble(amount) * 100));
		dispute.put("metadata", new JSONObject());
		dispute.put("charge", chb);

		JSONObject evidence = new JSONObject();
		evidence.put("refund_policy", JSONObject.NULL);
		evidence.put("enhanced_evidence", new JSONObject());
		evidence.put("customer_communication", JSONObject.NULL);
		evidence.put("shipping_date", JSONObject.NULL);
		evidence.put("billing_address", "CA");
		evidence.put("duplicate_charge_documentation", JSONObject.NULL);
		evidence.put("refund_policy_disclosure", JSONObject.NULL);
		evidence.put("shipping_carrier", JSONObject.NULL);
		evidence.put("service_documentation", JSONObject.NULL);
		evidence.put("uncategorized_text", JSONObject.NULL);
		evidence.put("cancellation_policy_disclosure", JSONObject.NULL);
		evidence.put("service_date", JSONObject.NULL);
		evidence.put("duplicate_charge_id", JSONObject.NULL);
		evidence.put("shipping_address", JSONObject.NULL);
		evidence.put("product_description", JSONObject.NULL);
		evidence.put("duplicate_charge_explanation", JSONObject.NULL);
		evidence.put("customer_purchase_ip", JSONObject.NULL);
		evidence.put("refund_refusal_explanation", JSONObject.NULL);
		evidence.put("uncategorized_file", JSONObject.NULL);
		evidence.put("shipping_documentation", JSONObject.NULL);
		evidence.put("access_activity_log", JSONObject.NULL);
		evidence.put("cancellation_rebuttal", JSONObject.NULL);
		evidence.put("customer_signature", JSONObject.NULL);
		evidence.put("cancellation_policy", JSONObject.NULL);
		evidence.put("receipt", JSONObject.NULL);
		evidence.put("customer_name", "Mohammad Sultani");
		evidence.put("customer_email_address", "sultanimohammad91@gmail.com");
		evidence.put("shipping_tracking_number", JSONObject.NULL);

		dispute.put("evidence", evidence);
		dispute.put("livemode", true);

		JSONObject evidenceDetails = new JSONObject();
		evidenceDetails.put("enhanced_eligibility", new JSONObject());
		evidenceDetails.put("has_evidence", false);
		evidenceDetails.put("due_by", 1791331199);
		evidenceDetails.put("submission_count", 0);
		evidenceDetails.put("past_due", false);

		dispute.put("evidence_details", evidenceDetails);
		dispute.put("created", epochSecond);

		JSONObject paymentMethodDetails = new JSONObject();
		paymentMethodDetails.put("type", "card");

		JSONObject card = new JSONObject();
		card.put("case_type", "chargeback");
		card.put("brand", "visa");
		card.put("network_reason_code", chargebackReason);
		card.put("network", "visa");
		paymentMethodDetails.put("card", card);
		dispute.put("payment_method_details", paymentMethodDetails);
		JSONArray balanceTransactions = new JSONArray();
		JSONObject balanceTransaction = new JSONObject();
		balanceTransaction.put("amount", -2146);
		balanceTransaction.put("available_on", epochSecond);
		balanceTransaction.put("exchange_rate", JSONObject.NULL);
		balanceTransaction.put("created", epochSecond);
		balanceTransaction.put("fee", 1500);
		balanceTransaction.put("reporting_category", "dispute");
		JSONArray feeDetails = new JSONArray();
		JSONObject feeDetail = new JSONObject();
		feeDetail.put("amount", 1500);
		feeDetail.put("application", JSONObject.NULL);
		feeDetail.put("description", "Dispute fee");
		feeDetail.put("currency", "usd");
		feeDetail.put("type", "stripe_fee");
		feeDetails.put(feeDetail);
		balanceTransaction.put("fee_details", feeDetails);
		balanceTransaction.put("description", "Chargeback withdrawal for " + chb);
		balanceTransaction.put("source", "du_1UHrFaDwr7i4Cg9VEAvncGbu");
		balanceTransaction.put("type", "adjustment");
		balanceTransaction.put("balance_type", "payments");
		balanceTransaction.put("currency", "usd");
		balanceTransaction.put("id", "txn_1UHrGSDwr7i4Cg9Vp2UljJMc");
		balanceTransaction.put("net", -3646);
		balanceTransaction.put("object", "balance_transaction");
		balanceTransaction.put("status", "available");
		balanceTransactions.put(balanceTransaction);
		dispute.put("balance_transactions", balanceTransactions);
		dispute.put("is_charge_refundable", false);
		dispute.put("enhanced_eligibility_types", new JSONArray());
		dispute.put("currency", "eur");
		dispute.put("payment_intent", ref);
		dispute.put("id", chb);
		dispute.put("object", "dispute");
		dispute.put("status", "needs_response");
		items.add(dispute.toString() + ",");
	}

	private void updateForEcommpayFraudData(List<String> items, String amount, String ref, LocalDateTime date) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject json = new JSONObject();
		json.put("payment_method_type", "visa");
		json.put("account_number", "431422******0056");
		json.put("bin", "420000");
		json.put("tr_amount", amount);
		json.put("project_name", "cosmoshop.earth");
		json.put("issuer_country", "US");
		json.put("row_updated_at", "2026-02-03 05:30:30");
		json.put("project_id", "123");
		json.put("payment_id", ref);
		json.put("operation_id", TestConstants.generateARN(15));
		json.put("received_on", date.toLocalDate());
		json.put("arn", TestConstants.generateARN(23));
		json.put("channel_amount_in_usd", amount);
		json.put("report_and_purchase_date_difference", 9);
		json.put("issuer_bank_name", "Intergalactic Bank");
		json.put("fraud_type", "6");
		json.put("customer_email", "earthling@earth.earth");
		json.put("customer_id", "earthling1232400");
		json.put("fraud_report_date", date.toLocalDate());
		json.put("purchase_date", date.toLocalDate());
		json.put("country_by_ip", "GB");
		json.put("tr_ccy", "EUR");
		items.add(json.toString() + ",");
    }

	private void updateForValitorpay(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "MerchantName,MerchantRegistrationNumber,TransactionID,Arn,PurchaseDate,CardNumber,"
				+ "TransactionType,Currency,GrossAmount,Interchange,Fees,NetAmount,AuthorizationNumber,TransactionCode,"
				+ "ReasonCode,CashbackAmount,TerminalID,DbaName,PhysicalTerminalID,SettlementNumber,AgreementID,"
				+ "PartnerID,OriginalAmount,OriginalCurrency,CardType,Scheme,SchemeFeeCurrency,SchemeFee,"
				+ "CardholderCountry,ReferenceData,TransactionLifeCycleID,AuthorizationDate";
		if (items.isEmpty()) {
			items.add(header);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		OffsetDateTime dateTime = date.atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
		String data = "Loichades Limited,HE424852,604012722215," + TestConstants.generateARN(23) + ","
				+ dateTime.format(formatter) + ",514609******2744,TRTYPE_15,EUR," + amount + ",0.75,3.5667,62.2133,"
				+ TestConstants.generateAuthCode()
				+ ",SALE05,0,0,30107043,RyDmarsianoo,30107043,49134600,448298,289,0,,DebitCard,MasterCard,EUR,0.8433,"
				+ "United States," + ref + ",MDJSRC5DZ0209,2026-02-09 00:00:00";
		items.add(data);
	}

	private void updateForPayretoReconciliaition(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "id,registrationId,referencedId,paymentType,paymentBrand,amount,currency,descriptor,"
				+ "merchantTransactionId,merchantInvoiceId,timestamp,result_code,result_description,"
				+ "resultDetails_ExtendedDescription,resultDetails_ConnectorTxID1,resultDetails_ConnectorTxID3,"
				+ "resultDetails_ConnectorTxID2,resultDetails_AcquirerResponse,resultDetails_clearingCutOff,card_bin,"
				+ "card_last4Digits,card_holder,card_expiryMonth,card_expiryYear,customer_givenName,customer_surname,"
				+ "customer_email,customer_ip,billing_street1,billing_city,billing_state,billing_postcode,"
				+ "billing_country";
		if (items.isEmpty()) {
			items.add(header);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
		OffsetDateTime dateTime = date.atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
		String data = "8ac9a4a89c951d55019cac5bea035eec,8ac9a4a29c712003019c7f39ced74b08,"
				+ TestConstants.getUniqueRef("BNK") + ",CP,VISA," + amount + ",EUR,proxuz.com," + ref + ",,"
				+ dateTime.format(formatter)
				+ ",000.000.000,Transaction succeeded,Transaction succeeded,d6ddb5de-9970-4ec3-ae8f-1e8b17fc2a09"
				+ ",,,000.000.000,,459981,3298,Santiago Manuel Navarro Barrios,12,2028,Santiago,Manuel Navarro Barrios,"
				+ "santiagomanuelnavarrobarrios@gmail.com,2a02:9130:a425:ddee:1896:338b:d264:cb76,123 Main St,"
				+ "Santa Cruz de Tenerife,ES,38005,ES,";
		items.add(data);
	}

	private void updateForPayxpertReconciliation(List<String> items, String amount, String ref, LocalDateTime date,
			String type) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject paymentMeanInfo = new JSONObject().put("cardExpireYear", "2029").put("is3DSecure", true)
				.put("cardHolderName", "Cornelis lastName").put("cardLevel", "platinum").put("cardSubType", "debit")
				.put("iinBankName", "banca march s.a.").put("iinCountry", "ES").put("cardBrand", "MCRD")
				.put("cardNumber", "547546XXXXXX0166").put("cardExpireMonth", "01");

		JSONObject json = new JSONObject().put("date", Utils.getEpochTime(date))
				.put("amount", Double.parseDouble(amount) * 100).put("customerIP", "87.117.100.26").put("orderID", ref)
				.put("referralID", 325185564).put("errorCode", "000").put("shopperEmail", "clam.verheijen@gmail.com")
				.put("orderDescription", JSONObject.NULL).put("transactionID", TestConstants.generateARN(9))
				.put("paymentType", "creditcard").put("shopperName", "Cornelis lastName")
				.put("paymentMeanInfo", paymentMeanInfo).put("currency", "EUR").put("subscriptionID", 0)
				.put("operation", type).put("status", "authorized");
		items.add(json.toString() + ",");
		log.info("Updating item size: " + items.size());
	}

	private void updateForPayxpertChargeback(List<String> items, String amount, String ref, LocalDateTime date,
			String chargebackReason) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject paymentMeanInfo = new JSONObject().put("cardExpireYear", "2028").put("is3DSecure", false)
				.put("cardHolderName", "Lidia Galkina").put("cardLevel", "premium").put("cardSubType", "debit")
				.put("iinBankName", "revolut ltd").put("iinCountry", "DE").put("cardBrand", "MCRD")
				.put("cardNumber", "420000XXXXXX0000").put("cardExpireMonth", "12");

		JSONObject jsonObject = new JSONObject().put("date", Utils.getEpochTime(date))
				.put("amount", String.valueOf(Double.parseDouble(amount) * 100)).put("customerIP", "80.187.113.70")
				.put("orderID", ref).put("referralID", JSONObject.NULL).put("errorCode", chargebackReason)
				.put("shopperEmail", "lidiagalkina@hotmail.com").put("orderDescription", JSONObject.NULL)
				.put("transactionID", ref).put("paymentType", "creditcard")
				.put("shopperName", "Lidia Galkina").put("paymentMeanInfo", paymentMeanInfo).put("currency", "EUR")
				.put("subscriptionID", 0).put("operation", "chargeback").put("status", "authorized");
		items.add(jsonObject.toString() + ",");
	}

	private void updateForPayxpertFraud(List<String> items, String amount, String ref, LocalDateTime date,
			String chargebackReason) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject paymentMeanInfo = new JSONObject().put("cardExpireYear", "2028").put("is3DSecure", false)
				.put("cardHolderName", "Lidia Galkina").put("cardLevel", "premium").put("cardSubType", "debit")
				.put("iinBankName", "revolut ltd").put("iinCountry", "DE").put("cardBrand", "MCRD")
				.put("cardNumber", "420000XXXXXX0000").put("cardExpireMonth", "12");

		JSONObject jsonObject = new JSONObject().put("date", Utils.getEpochTime(date))
				.put("amount", String.valueOf(Double.parseDouble(amount) * 100)).put("customerIP", "80.187.113.70")
				.put("orderID", ref).put("referralID", JSONObject.NULL).put("errorCode", chargebackReason)
				.put("shopperEmail", "lidiagalkina@hotmail.com").put("orderDescription", JSONObject.NULL)
				.put("transactionID", ref).put("paymentType", "creditcard")
				.put("shopperName", "Lidia Galkina").put("paymentMeanInfo", paymentMeanInfo).put("currency", "EUR")
				.put("subscriptionID", 0).put("operation", "fraud").put("status", "authorized");
		items.add(jsonObject.toString() + ",");
	}

	private void updateForEcommpayReconciliationData(List<String> items, String amount, String ref,
			LocalDateTime date) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("billing_amount", amount);
		jsonObject.put("account_number", "535456******3843");
		jsonObject.put("operation_type", "purchase");
		jsonObject.put("proc_region", "EU/SEPA");
		jsonObject.put("billing_conversion_rate", "1.000000");
		jsonObject.put("project_url", "https://uk-road.com");
		jsonObject.put("operation_amount", amount);
		jsonObject.put("issuer_country", "GB");
		jsonObject.put("card_holder", "RABBIA MAQSOOD");
		jsonObject.put("total_interchange_fee", "-0.07");
		jsonObject.put("auth_appr_code", TestConstants.generateAuthCode());
		jsonObject.put("project_id", "149891");
		jsonObject.put("provider_payment_id", "0010000149484320");
		jsonObject.put("payment_id", ref);
		jsonObject.put("payment_description", JSONObject.NULL);
		jsonObject.put("operation_status", "success");
		jsonObject.put("billing_currency", "GBP");
		jsonObject.put("operation_id", "41102010188429");
		jsonObject.put("arn", TestConstants.generateARN(23));
		jsonObject.put("mcc_code", "7375");
		jsonObject.put("terminal_id", "70013974");
		jsonObject.put("transaction_id", "41102010168405");
		jsonObject.put("total_msc_fee", "-1.15");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		OffsetDateTime dateTime = date.atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
		jsonObject.put("operation_completed_at", dateTime.format(formatter));
		jsonObject.put("hold_amount", -3.9);
		jsonObject.put("card_product", "Debit Other Embossed MasterCard Card");
		jsonObject.put("clearing_msc_fee", "-0.975000");
		jsonObject.put("merchant_name", "EFLOW, MOTORCAR TECH");
		jsonObject.put("legal_country", "GB");
		jsonObject.put("operation_currency", "EUR");
		jsonObject.put("payment_method_name", "mastercard");
		jsonObject.put("rrn", "001137374198");
		jsonObject.put("security_level", "NON-SEC");
		jsonObject.put("auth_msc_fee", "-0.170000");
		jsonObject.put("product_type", "Consumer");
		jsonObject.put("tran_region", "domestic");
		jsonObject.put("total_scheme_fee", "-0.17");
		jsonObject.put("customer_id", "cs_1adeee80");
		items.add(jsonObject + ",");
	}

	public void updateForEcommpayChargeback(List<String> items, String amount, String ref, LocalDateTime date,
			String chargebackReason) {
		JSONObject data = new JSONObject();
		if (items.isEmpty()) {
			items.add("[");
		}
		data.put("chargeback_id", TestConstants.getUniqueRef("CBK-"));
		data.put("case_id", TestConstants.getUniqueRef("CASE-"));
		data.put("order_id", ref);
		data.put("arn", TestConstants.generateARN(23));
		data.put("reason_code", chargebackReason);
		DateTimeFormatter cbkFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		data.put("report_date", cbkFormatter.format(date));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		OffsetDateTime dateTime = date.atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
		data.put("tr_date_time", dateTime.format(formatter));
		data.put("chb_amount", amount);
		data.put("chb_ccy", "EUR");
		data.put("channel_currency", "EUR");
		data.put("channel_amount", amount);
		data.put("eci_sli", "05");
		data.put("auth_code", TestConstants.generateAuthCode());
		items.add(data + ",");
	}

	public void updateForNmiReconciliation(boolean isCapture, String ref, LocalDateTime date, String amount,
			List<String> items) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject json = new JSONObject().put("surcharge", JSONObject.NULL).put("country", "NL")
				.put("account_type", JSONObject.NULL).put("national_tax_amount", JSONObject.NULL)
				.put("check_account", JSONObject.NULL).put("address_1", "Singel 101")
				.put("cavv_result", JSONObject.NULL).put("discount_amount", JSONObject.NULL).put("address_2", "117 NG")
				.put("shipping_city", JSONObject.NULL).put("shipping_address_1", JSONObject.NULL)
				.put("entry_mode", "Keyed").put("alternate_tax_amount", JSONObject.NULL).put("eci", JSONObject.NULL)
				.put("shipping_address_2", JSONObject.NULL).put("network_token_used", "N")
				.put("summary_commodity_code", JSONObject.NULL).put("card_balance", JSONObject.NULL)
				.put("xid", JSONObject.NULL).put("shipping", 0.03).put("misc_fee_name", JSONObject.NULL)
				.put("misc_fee", JSONObject.NULL);

		JSONArray actionArray = new JSONArray();

		JSONObject authAction = new JSONObject().put("date", date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
				.put("amount", amount).put("response_code", 100).put("action_type", "auth").put("batch_id", 0)
				.put("device_nickname", JSONObject.NULL).put("ip_address", "49.205.173.37").put("source", "api")
				.put("processor_batch_id", JSONObject.NULL).put("processor_response_text", JSONObject.NULL)
				.put("api_method", "direct_post").put("success", 1).put("tap_to_mobile", false)
				.put("response_text", "SUCCESS").put("processor_response_code", JSONObject.NULL)
				.put("device_license_number", JSONObject.NULL).put("username", "wlpaymentsTest");

		JSONObject captureAction = new JSONObject()
				.put("date", date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))).put("amount", amount)
				.put("response_code", 100).put("action_type", "capture").put("batch_id", 0)
				.put("device_nickname", JSONObject.NULL).put("ip_address", "23.97.197.249").put("source", "api")
				.put("processor_batch_id", JSONObject.NULL).put("processor_response_text", JSONObject.NULL)
				.put("api_method", "direct_post").put("success", 1).put("tap_to_mobile", false)
				.put("response_text", "SUCCESS").put("processor_response_code", JSONObject.NULL)
				.put("device_license_number", JSONObject.NULL).put("username", "wlpaymentsTest");

		actionArray.put(authAction);
		if (isCapture) {
			actionArray.put(captureAction);
		}

		json.put("action", actionArray);

		json.put("tip", JSONObject.NULL).put("state", JSONObject.NULL).put("fax", JSONObject.NULL)
				.put("drivers_license_dob", JSONObject.NULL).put("transaction_id", 12078410631L)
				.put("network_token_created", false).put("shipping_company", JSONObject.NULL)
				.put("shipping_first_name", JSONObject.NULL).put("cc_bin", 411111)
				.put("network_token_cryptogram_created", false).put("directory_server_id", JSONObject.NULL)
				.put("tax", 0.03).put("transaction_type", "cc").put("cell_phone", JSONObject.NULL)
				.put("network_token", JSONObject.NULL).put("cavv", JSONObject.NULL).put("cc_exp", 1035)
				.put("condition", "pendingsettlement").put("customertaxid", JSONObject.NULL)
				.put("phone", "31-232567456").put("cardholder_auth", JSONObject.NULL).put("ponumber", JSONObject.NULL)
				.put("customerid", JSONObject.NULL).put("token_or_card_number", "4xxxxxxxxxxx1111")
				.put("order_id", ref + ".C").put("shipping_country", JSONObject.NULL)
				.put("shipping_postal_code", JSONObject.NULL).put("convenience_fee", JSONObject.NULL)
				.put("network_token_expiration", JSONObject.NULL).put("shipping_date", JSONObject.NULL)
				.put("shipping_state", JSONObject.NULL).put("city", "Amsterdam")
				.put("order_description", JSONObject.NULL).put("shipping_phone", JSONObject.NULL)
				.put("shipping_carrier", JSONObject.NULL).put("sec_code", JSONObject.NULL).put("cc_type", "Visa")
				.put("partial_payment_id", JSONObject.NULL).put("cc_issue_number", JSONObject.NULL)
				.put("authorization_code", TestConstants.generateAuthCode()).put("avs_response", "N")
				.put("tracking_number", ref + ".C").put("processor_id", "bluesnap3dstest")
				.put("company", JSONObject.NULL).put("social_security_number", JSONObject.NULL).put("currency", "EUR")
				.put("cc_start_date", JSONObject.NULL).put("cc_number", "4xxxxxxxxxxx1111").put("first_name", "Test")
				.put("email", "john@celerispay.com").put("duty_amount", JSONObject.NULL).put("website", JSONObject.NULL)
				.put("partial_payment_balance", JSONObject.NULL).put("vat_tax_amount", JSONObject.NULL)
				.put("last_name", "Test").put("cc_hash", "f6c609e195d9d4c185dcc8ca662f0180")
				.put("drivers_license_number", JSONObject.NULL).put("signature_image", JSONObject.NULL)
				.put("card_available_balance", JSONObject.NULL).put("check_aba", JSONObject.NULL)
				.put("check_hash", JSONObject.NULL).put("three_ds_version", JSONObject.NULL).put("is_aft", false)
				.put("account_holder_type", JSONObject.NULL).put("cash_discount", JSONObject.NULL)
				.put("shipping_email", JSONObject.NULL).put("platform_id", JSONObject.NULL)
				.put("shipping_last_name", JSONObject.NULL).put("check_name", JSONObject.NULL)
				.put("vat_tax_rate", JSONObject.NULL).put("postal_code", 1000)
				.put("drivers_license_state", JSONObject.NULL).put("csc_response", "N");
		items.add(json.toString() + ", ");
	}

	private void updateForTruevoReconciliationData(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "Billing Method,Customer,Card Acceptor,Processing Date,Transaction Type,Transaction Status,Scheme,BIN ,Last 4,Issuing Country,Billing Region,Card Product,Card Type,Merchant ID,Terminal ID,MCC,RRN,ARN,Original RRN,Auth Code,Merchant Reference,Gateway Unique ID,ECI,Processing Currency,Processing Transaction Amount,Settlement Currency,Settlement Transaction Amount,Reserve Amount,Interchange Fee,Scheme Fee,Acquirer Fee,Settlement Net Amount,partner_name,";
		if (items.isEmpty()) {
			items.add(header);
		}
		String data = "Blended,Test Algo,wildflings.com_ecomm,"
				+ date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ",PURCHASE,"
				+ "Complete,Visa,400000,1000,USA,Inter,Consumer,Debit,22008521,30000425,7273,525101330598,"
				+ TestConstants.generateARN(23) + ",525101330598, " + TestConstants.generateAuthCode() + "," + ref
				+ ".C,,,EUR," + amount + ",EUR," + amount + ",0.73,0,0,0.92578,12.88422,Connfido B.V MDR LR,";
		items.add(data);
	}

	public void updateForCredoraxReconciliationData(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "merchant_name,contract_id,mid,descriptor,merchant_country,transaction_date,posting_date,"
				+ "transaction_type,orig_transaction_currency,orig_transaction_amount,transaction_region,card_scheme,"
				+ "card_type,card_brand,card_product,card_number,expiry_date,card_holder_country,request_id,"
				+ "searchable_code,submerchant_id,authorization_code,rrn,arn,payment_channel,settlement_currency,"
				+ "gross_transaction_amount,merchant_fixed_transaction_fee,merchant_interchange_amount,"
				+ "merchant_card_scheme_fees,merchant_acquiring_fee,merchant_discount_rate,net_settlement_amount,"
				+ "payment_id,B20,response_id,other_transactional_charges,Account_ID,";
		if (items.isEmpty()) {
			items.add(header);
		}
		String data = "Sam Media B.V.,001-CM-584651,R0102004,XR Academy*3197010208226,NLD,"
				+ date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + LocalDate.now()
				+ ",Purchase,EUR," + amount + ",Interregional,Visa,Debit,Visa Classic,consumer,444494******9774,1231,"
				+ "SAU,UNQ-REF202601201617479,,,6359b7,'601201809003,'" + TestConstants.generateARN(23) + ",ENET,EUR,"
				+ "0.02,-0.15,-0.00023,-0.14107,-0.0007,,-0.272,XZZ519f85fcf0bdf529RO7F3HTEBRHQZ,,N@LZN7AT8X,,"
				+ "CEID-0000818609,";
		items.add(data);
	}

	public void updateForCredoraxFraudData(List<String> items, String amount, String ref, LocalDateTime date,
			String chargebackReason) {
		String header = "descriptor,country_code,trans_mcc,fraud_type,amount,issuer_bin,trans_date,card_number,arn,"
				+ "payment_channel,request_id,searchable_code,target_country_code,status_code,payment_id,Report_Date,Ac"
				+ "count_ID,";
		if (items.isEmpty()) {
			items.add(header);
		}
		String data = "XR ACADEMY*3197010208226,Netherlands,5817,6," + amount + ",476892,"
				+ date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ",476892******2233,'"
				+ TestConstants.generateARN(23) + ",ENET," + TestConstants.getUniqueRef("REF-KEY-")
				+ ",,SVN,1,L2@APGHNW6," + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				+ ",CEID-0000818609,";
		items.add(data);
	}

	public static void updateForBraintreeReconciliation(List<String> items, String amount, String ref,
			LocalDateTime date, boolean capture) {
		if (items.isEmpty()) {
			items.add("[");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");
		OffsetDateTime dateTime = date.atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
		JSONObject root = new JSONObject();
		JSONObject node = new JSONObject();
		String formatted = dateTime.format(formatter);
		node.put("createdAt", formatted);
		JSONArray statusHistory = new JSONArray();

		if (capture) {
			JSONObject status1 = new JSONObject();
			status1.put("amount", new JSONObject().put("value", amount).put("currencyCode", "EUR"));
			status1.put("source", JSONObject.NULL);
			status1.put("status", "SETTLED");
			status1.put("timestamp", formatted);
			statusHistory.put(status1);

			JSONObject status2 = new JSONObject();
			status2.put("amount", new JSONObject().put("value", amount).put("currencyCode", "EUR"));
			status2.put("source", "API");
			status2.put("status", "SUBMITTED_FOR_SETTLEMENT");
			status2.put("timestamp", formatted);
			statusHistory.put(status2);
		}
		JSONObject status3 = new JSONObject();
		status3.put("amount", new JSONObject().put("value", amount).put("currencyCode", "EUR"));
		status3.put("source", "API");
		status3.put("status", "AUTHORIZED");
		status3.put("timestamp", formatted);
		statusHistory.put(status3);

		node.put("statusHistory", statusHistory);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value", amount);
		jsonObject.put("currencyCode", "EUR");

		node.put("amount", jsonObject);
		node.put("orderId", ref + ".C");

		JSONObject paymentMethod = new JSONObject();
		paymentMethod.put("createdAt", formatted);
		paymentMethod.put("usage", "MULTI_USE");
		paymentMethod.put("id", "cGF5bWVudG1ldGhz");
		JSONObject processorAuthorizationResponse = new JSONObject();
		processorAuthorizationResponse.put("authorizationId", TestConstants.generateAuthCode());
		processorAuthorizationResponse.put("retrievalReferenceNumber", "1234567");
		processorAuthorizationResponse.put("legacyCode", "1000");
		processorAuthorizationResponse.put("message", "Approved");

		node.put("processorAuthorizationResponse", processorAuthorizationResponse);
		node.put("id", TestConstants.generateARN(12));
		node.put("merchantAccountId", "mambotechnologiessl");
		node.put("status", "SETTLED");
		root.put("node", node);
		items.add(root.toString() + ", ");
	}

	public String getFilePath(String acquirer, String reconciliationType) {
		String filePath = "";
		if (acquirer.equalsIgnoreCase("acquired") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/acquired.json";
		} else if (acquirer.equalsIgnoreCase("truevo") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/truevo.csv";
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("fraud")) {
			filePath = "/home/ashish/TestFiles/ecommpay_fraud.json";
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("payment")) {
			filePath = "/home/ashish/TestFiles/ecommpay_payment.json";
		} else if (acquirer.equalsIgnoreCase("ecommpay") && reconciliationType.equalsIgnoreCase("chargeback")) {
			filePath = "/home/ashish/TestFiles/ecommpay_chargeback.json";
		} else if (acquirer.equalsIgnoreCase("credorax") && reconciliationType.equalsIgnoreCase("fraud")) {
			filePath = "/home/ashish/TestFiles/credorax_fraud.csv";
		} else if (acquirer.equalsIgnoreCase("credorax") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/credorax_payment.csv";
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("payment")) {
			filePath = "/home/ashish/TestFiles/payxpert_payment.json";
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/payxpert_capture.json";
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("chargeback")) {
			filePath = "/home/ashish/TestFiles/payxpert_chargeback.json";
		} else if (acquirer.equalsIgnoreCase("payxpert") && reconciliationType.equalsIgnoreCase("fraud")) {
			filePath = "/home/ashish/TestFiles/payxpert_fraud.json";
		} else if (acquirer.equalsIgnoreCase("payreto") && reconciliationType.equalsIgnoreCase("payment")) {
			filePath = "/home/ashish/TestFiles/payreto_reconciliation.csv";
		} else if (acquirer.equalsIgnoreCase("rapyd") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/rapyd_reconciliation.csv";
		} else if (acquirer.equalsIgnoreCase("braintree") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/braintree.json";
		} else if (acquirer.equalsIgnoreCase("nmi") && reconciliationType.equalsIgnoreCase("capture")) {
			filePath = "/home/ashish/TestFiles/nmi_capture.json";
		} else if (acquirer.equalsIgnoreCase("stripe") && reconciliationType.equalsIgnoreCase("chargeback")) {
			filePath = "/home/ashish/TestFiles/stripe_chargeback.json";
		}
		return filePath;
	}

	public String updateListToCsv(List<String> items, String acquirer) {
		StringBuilder sb = new StringBuilder();
		for (String item : items) {
			sb.append(item);
			sb.append("\n");
		}
		if (TestConstants.JSON_ACQUIRER.contains(acquirer)) {
			sb.setLength(sb.length() - 2);
			sb.append("\n]");
		}
		return sb.toString();
	}

	public void writeDataToFile(String path, String data) {
		File file = new File(path);
		System.out.println();
		try {
			if (!file.exists()) {
				Files.createFile(file.toPath());
			}
		} catch (Exception e) {
			log.error("Error occurred while creating file: {}", e.getMessage());
		}
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(data);
			log.info("Data has been written to: {}", path);
		} catch (Exception e) {
			log.error("Issue occurred while writing: {}", e.getMessage());
		}
	}
}
