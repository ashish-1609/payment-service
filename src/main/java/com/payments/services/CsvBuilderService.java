package com.payments.services;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.payments.commons.TestConstants;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CsvBuilderService {

	public void prepareReconciliationFile(List<String> items, String acquirer, String amount, String ref) {
		updateFileData(items, acquirer, amount, ref);
	}

	private void updateFileData(List<String> items, String acquirer, String amount, String ref) {
		LocalDateTime date = LocalDateTime.now();
		switch (acquirer) {
			case "acquired" :
				// "/home/ashish/TestFiles/acquired.json";
				break;
			case "truevo" :
				updateTruevoReconciliationData(items, amount, ref, date);
				break;
			case "ecommpay" :
				updateEcommpayReconciliationData(items, amount, ref, date);
				break;
			case "credorax" :
				updateCredoraxFraudData(items, amount, ref, date);
				break;
			case "payxpert" :
				updatePayxpertChargeback(items, amount, ref, date);
			default :
				break;
		}
	}

	private void updatePayxpertChargeback(List<String> items, String amount, String ref, LocalDateTime date) {
		if (items.isEmpty()) {
			items.add("[");
		}
		JSONObject paymentMeanInfo = new JSONObject().put("cardExpireYear", "2028").put("is3DSecure", false)
				.put("cardHolderName", "Lidia Galkina").put("cardLevel", "premium").put("cardSubType", "debit")
				.put("iinBankName", "revolut ltd").put("iinCountry", "DE").put("cardBrand", "MCRD")
				.put("cardNumber", "420000XXXXXX0000").put("cardExpireMonth", "12");

		ZoneId zoneId = ZoneOffset.systemDefault();
		Instant instant = date.atZone(zoneId).toInstant();
		ZoneOffset offset = zoneId.getRules().getOffset(instant);
		JSONObject jsonObject = new JSONObject().put("date", String.valueOf(date.toEpochSecond(offset)))
				.put("amount", String.valueOf(Double.parseDouble(amount) * 100)).put("customerIP", "80.187.113.70")
				.put("orderID", ref).put("referralID", JSONObject.NULL).put("errorCode", "000")
				.put("shopperEmail", "lidiagalkina@hotmail.com").put("orderDescription", JSONObject.NULL)
				.put("transactionID", TestConstants.generateARN(9)).put("paymentType", "creditcard")
				.put("shopperName", "Lidia Galkina").put("paymentMeanInfo", paymentMeanInfo).put("currency", "EUR")
				.put("subscriptionID", 0).put("operation", "chargeback").put("status", "authorized");

		items.add(jsonObject.toString() + ",");
	}

	private void updateEcommpayReconciliationData(List<String> items, String amount, String ref, LocalDateTime date) {
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
		jsonObject.put("auth_appr_code", "91f806");
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

	public void updateEcommpayChargeback(List<String> items, String amount, String ref, LocalDateTime date) {
		JSONObject data = new JSONObject();
		if (items.isEmpty()) {
			items.add("[");
		}
		data.put("chargeback_id", TestConstants.getUniqueRef("CBK-"));
		data.put("case_id", TestConstants.getUniqueRef("CASE-"));
		data.put("order_id", ref);
		data.put("arn", TestConstants.generateARN(23));
		data.put("reason_code", "4837");
		data.put("report_date", LocalDate.now().toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
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

	private void updateTruevoReconciliationData(List<String> items, String amount, String ref, LocalDateTime date) {
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

	public void updateCredoraxReconciliationData(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "merchant_name,contract_id,mid,descriptor,merchant_country,transaction_date,posting_date,transaction_type,orig_transaction_currency,orig_transaction_amount,transaction_region,card_scheme,card_type,card_brand,card_product,card_number,expiry_date,card_holder_country,request_id,searchable_code,submerchant_id,authorization_code,rrn,arn,payment_channel,settlement_currency,gross_transaction_amount,merchant_fixed_transaction_fee,merchant_interchange_amount,merchant_card_scheme_fees,merchant_acquiring_fee,merchant_discount_rate,net_settlement_amount,payment_id,B20,response_id,other_transactional_charges,Account_ID,";
		if (items.isEmpty()) {
			items.add(header);
		}
		String data = "Sam Media B.V.,001-CM-584651,R0102004,XR Academy*3197010208226,NLD,"
				+ date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + LocalDate.now()
				+ ",Purchase,EUR," + amount
				+ ",Interregional,Visa,Debit,Visa Classic,consumer,444494******9774,1231,SAU,UNQ-REF202601201617479,,,6359b7,'601201809003,'"
				+ TestConstants.generateARN(23)
				+ ",ENET,EUR,0.02,-0.15,-0.00023,-0.14107,-0.0007,,-0.272,XZZ519f85fcf0bdf529RO7F3HTEBRHQZ,,N@LZN7AT8X,,CEID-0000818609,";
		items.add(data);
	}

	public void updateCredoraxFraudData(List<String> items, String amount, String ref, LocalDateTime date) {
		String header = "descriptor,country_code,trans_mcc,fraud_type,amount,issuer_bin,trans_date,card_number,arn,payment_channel,request_id,searchable_code,target_country_code,status_code,payment_id,Report_Date,Account_ID,";
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

	public String getFilePath(String acquirer) {
		String filePath = "";
		switch (acquirer) {
			case "acquired" :
				filePath = "/home/ashish/TestFiles/acquired.json";
				break;
			case "truevo" :
				filePath = "/home/ashish/TestFiles/truevo.csv";
				break;
			// case "ecommpay" :
			// filePath = "/home/ashish/TestFiles/ecommpay_fraud.json";
			// break;
			case "ecommpay" :
				filePath = "/home/ashish/TestFiles/ecommpay_payment.json";
				break;
			case "credorax" :
				filePath = "/home/ashish/TestFiles/credorax_fraud.csv";
				break;
			case "payxpert" :
				filePath = "/home/ashish/TestFiles/payxpert_chargeback.json";
				break;
			default :
				break;
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
			sb.append("\n]");
		}
		return sb.toString();
	}

	public void writeDataToFile(String path, String data) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				Files.createFile(file.toPath());
			}
		} catch (Exception e) {
			log.error("Error occurred while writing: {}", e.getMessage());
		}
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(data);
		} catch (Exception e) {
			log.error("Issue occurred while writing: {}", e.getMessage());
		}
	}
}
