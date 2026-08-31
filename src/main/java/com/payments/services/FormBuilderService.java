package com.payments.services;

import java.awt.*;
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

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FormBuilderService {

	public static final String PATH_OF_FILE = "/home/ashish/paymentform.html";

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
		log.warn("Removing Existing File...");
		try {
			Files.delete(Path.of(PATH_OF_FILE));
		} catch (IOException e) {
			log.error("Error while deleting existing file");
		}
	}

	public void writeToFile(String html, boolean openInBrowser, String endpoint, String data) {
		removeFile();
		try (FileWriter fw = new FileWriter(PATH_OF_FILE)) {
			fw.write(html);
			log.info("File Created......");
			if (openInBrowser) {
				openInBrowser(new File(PATH_OF_FILE));
			} else {
				postWithoutBrowser(endpoint, data);
			}
		} catch (IOException e) {
			log.error("Error writing to file");
		}

	}

	public void openInBrowser(File file) {
		log.info("Opening File: {}", file.getAbsolutePath());
		try {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("linux")) {
				new ProcessBuilder("google-chrome", file.getAbsolutePath()).start();
			} else if (os.contains("mac")) {
				new ProcessBuilder("open", file.getAbsolutePath()).start();
			} else if (os.contains("windows")) {
				Desktop.getDesktop().browse(file.toURI());
			} else {
				log.warn("Unsupported OS for browser opening");
			}
		} catch (IOException e) {
			log.error("{}: {}", e, e.getMessage());
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
			log.info("POST Response: {}", responseCode);

		} catch (Exception e) {
			log.error("Error posting without browser: {}", e.getMessage());
		}
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
