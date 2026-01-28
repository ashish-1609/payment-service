package com.payments;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class FormBuilder {

	public static final String PATH_OF_FILE = "C:\\Users\\Ashish Mishra\\Desktop\\paymentform.html";

	public void createForm(Map<String, Object> map) {
		String endpoint = map.get("endpoint").toString();
		String data = map.get("data").toString();
		StringBuilder htmlForm = new StringBuilder();
		htmlForm.append("<!DOCTYPE html>\n").append("<html>\n").append("<head runat=\"server\">\n")
				.append("    <title>Redirect</title>\n").append("</head>\n")
				.append("<body OnLoad=\"OnLoadEvent();\">\n")
				.append("    <form target=\"_top\" name=\"form1\" action=\"").append(endpoint)
				.append("\" method=\"post\">\n").append("        <input type=\"hidden\" name=\"data\" value='")
				.append(data).append("' id=\"data\">\n").append("    </form>\n")
				.append("    <script language=\"JavaScript\">\n").append("        function OnLoadEvent() {\n")
				.append("            document.form1.submit();\n").append("        };\n").append("   \n")
				.append("    </script>\n").append("</body>\n").append("</html>");
		boolean openInBrowser = false;
		writeToFile(htmlForm.toString(), openInBrowser, endpoint, data);
	}

	public void removeFile() {
		System.out.println("Removing Existing File...");
		try {
			Files.delete(Path.of(PATH_OF_FILE));
		} catch (IOException e) {
			System.out.println("Error while deleting existing file");
		}
	}

	public void writeToFile(String html, boolean openInBrowser, String endpoint, String data) {
		removeFile();
		try (FileWriter fw = new FileWriter(PATH_OF_FILE)) {
			fw.write(html);
			System.out.println("File Created......");
			if (openInBrowser) {
				openInBrowser(new File(PATH_OF_FILE));
			} else {
				postWithoutBrowser(endpoint, data);
			}
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}

	}

	public void openInBrowser(File file) {
		System.out.println("Opening File: " + file.getAbsolutePath());
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(file.toURI());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("Desktop is not supported");
		}
	}

	public void postWithoutBrowser(String endpoint, String data) {
		try {
			URL url = new URL(endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String payload = "data=" + URLEncoder.encode(data, "UTF-8");

			try (OutputStream os = conn.getOutputStream()) {
				os.write(payload.getBytes());
			}

			int responseCode = conn.getResponseCode();
			System.out.println("POST Response: " + responseCode);

		} catch (Exception e) {
			System.out.println("Error posting without browser");
			e.printStackTrace();
		}
	}

	public String curlChargeBackHelpRequest(String txnAmount) {
		Random random = new Random();

		return "curl --location 'http://localhost:8080/ngp/chargebackhelp' \\\n"
				+ "--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NDQ3OTg2ODIsImV4cCI6MTc0NDgwMTY4MiwianRpIjoiODQzZTVhZDQtNjFmNC00OTI4LTljZjAtY2IzY2ZmMWI4MWIyIn0.9nqwhx6tw-DioyUC-noksnseUPUssSA3oPa6BGU4qJo' \\\n"
				+ "--header 'Content-Type: application/json' \\\n" + "--data '{\n" + "    \"entity\": \"alerts\",\n"
				+ "    \"event\": \"received\",\n" + "    \"data\": [\n" + "        {\n" + "            \"id\": \""
				+ random.nextInt(9999) + random.nextInt(9999) + "\",\n"
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

	public String nethoneCurlRequest(String txnAmount, String ref) {
		Random random = new Random();
		BigInteger decimal = BigDecimal.valueOf(Double.parseDouble(txnAmount)).multiply(new BigDecimal(100))
				.toBigInteger();
		return "curl -X POST 'http://localhost:8080/ngp/nethone' \\\n" + "-H \"Content-Type: application/json\" \\\n"
				+ "-H \"Authorization: Basic c3VuaWxAY2VsZXJpc3BheS5jb206eWpeMzRTamh6QVVFbm03\" \\\n" + "-d '{\n"
				+ "   \"id_str\": \"" + random.nextInt(9899) + random.nextInt(9989) + "\",\n"
				+ "   \"clientIP\": \"34.248.23.135\",\n" + "   \"created_at\":  \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC)
						.format(Instant.now().plusSeconds(30))
				+ "Z\",\n" + "   \"id\": " + random.nextInt(9999) + random.nextInt(9999) + ",\n"
				+ "   \"type\": \"TRANSACTION_ALERT\",\n" + "   \"body\": {\n" + "   \"transaction_id\": null,\n"
				+ "   \"number_last4\": \"0000\",\n" + "   \"transaction_timestamp\": \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC).format(Instant.now())
				+ "Z\",\n" + "   \"source\": 1,\n" + "   \"is_3d_secure\": false,\n"
				+ "   \"issuer\": \"NatWest - Debit\",\n" + "   \"alert_type\": \"dispute\",\n"
				+ "   \"reason_code\": \"\",\n" + "   \"refund_value\": {\n" + "       \"amount\":" + decimal + ",\n"
				+ "       \"amount_decimal\": \"" + txnAmount + "\",\n" + "       \"currency\": \"EUR\"\n" + "    },\n"
				+ "    \"billing_descriptor\": \"XR ACADEMY\",\n" + "    \"authorization_code\": \"\",\n"
				+ "    \"merchant_reference\": null,\n" + "    \"bin_number\": \"420000\",\n"
				+ "    \"source_id\": \"634DIGXEFHTW918T5JFFBJ3E7\",\n" + "    \"transaction_value\": {\n"
				+ "       \"amount\":" + decimal + ",\n" + "       \"amount_decimal\": \"" + txnAmount + "\",\n"
				+ "       \"currency\": \"EUR\"\n" + "    },\n" + "    \"transaction_reference\": \"" + ref + "\",\n"
				+ "    \"arn\": null,\n" + "    \"alert_timestamp\": \""
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC)
						.format(Instant.now().plusSeconds(30))
				+ "Z\"\n" + "  }\n" + "}'";
	}

	public String ethocaCurlRequest(String txnAmount, String ref) {
		return "curl 'http://localhost:8080/ngp/external-chargeback' -H 'Content-Type: application/xml' -d "
				+ "'<EthocaAlertNotification>\n" + "   <Username>support@celerispay.com</Username>\n"
				+ "   <Password>amyZF9%9Uto@v$H</Password>\n" + "   <Alert>\n" + "           <EthocaID>"
				+ UUID.randomUUID().toString().toUpperCase() + "</EthocaID>\n" + "           <AlertTimestamp>"
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneOffset.UTC)
						.format(Instant.now().plusSeconds(30))
				+ "Z" + "</AlertTimestamp>\n" + "           <Age>42</Age>\n"
				+ "           <Issuer>CARD_ISSUER</Issuer>\n" + "           <CardNumber>5500000000000004</CardNumber>\n"
				+ "           <TransactionTimestamp>"
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneOffset.UTC)
						.format(Instant.now())
				+ "Z</TransactionTimestamp>\n" + "           <MerchantDescriptor>test</MerchantDescriptor>\n"
				+ "           <EthocaMerchantID>TEM011220001</EthocaMerchantID>\n" + "           <Amount>" + txnAmount
				+ "</Amount>\n" + "           <Currency>EUR</Currency>\n"
				+ "           <TransactionType>eCommerce</TransactionType>\n"
				+ "           <InitiatedBy>issuer</InitiatedBy>\n" + "           <Liability>no</Liability>\n"
				+ "           <MerchantName>ABC ONLINE</MerchantName>\n"
				+ "           <PartnerMerchantID>456</PartnerMerchantID>\n" + "           <TransactionId>" + ref
				+ "</TransactionId>\n" + "           <ChargebackReasonCode>UA11</ChargebackReasonCode>\n"
				+ "           <ChargebackAmount>" + txnAmount + "</ChargebackAmount>\n"
				+ "           <ChargebackCurrency>EUR</ChargebackCurrency>\n" + "   </Alert>\n"
				+ "</EthocaAlertNotification>'\n";
	}
}
