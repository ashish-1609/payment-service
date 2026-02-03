package com.payments;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.gateway.authorization.AuthorizationFactory;
import com.gateway.authorization.CaptureDTO;
import com.gateway.constants.enums.BeneficiaryTypeCodeEnum;
import com.gateway.constants.enums.CardTypeEnum;
import com.gateway.constants.enums.PaymentModeEnum;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.AccountDTO;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.CustomerDTO;
import com.gateway.payment.DecryptPaymentTokenRequestDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.PaymentFactory;
import com.gateway.payment.PayoutFactory;
import com.gateway.payment.TransactionDTO;
import com.gateway.payment.UrlDTO;
import com.gateway.subscription.InstallmentsDTO;
import com.gateway.subscription.PlanDTO;

import com.payments.services.FormBuilderService;
import net.datafaker.Faker;

public class MainController {
//
//	public static final Random random = new Random();
//	static MerchantDTO merchantDetails;
//	static Certificate certificate = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);
//
//	static {
//		String customerID = TestConstants.getUniqueRef("CUS-TEST");
//		merchantDetails = new MerchantDTO(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID, customerID);
//	}
//
//	public static void main(String[] args) throws ApplicationError, IOException, InterruptedException {
//		List<String> items = new ArrayList<>();
//		String acquirer = "truevo";
//		for (int i = 0; i < 1; i++) {
//			transaction(items, acquirer);
//			System.out.println(i+".) Transaction Successful.");
//		}
//		if (acquirer.equalsIgnoreCase("ecommpay")) {
//			items.add("]");
//		}
////		CsvBuilderService.writeDataToFile(CsvBuilderService.getFilePath(acquirer),
////				CsvBuilderService.updateListToCsv(items));
//	}
//
//	public static void captureTxn(String txnRef, String currency, String amount) throws ApplicationError {
//		CaptureDTO captureDTO = new CaptureDTO(txnRef, currency);
//		captureDTO.setAmount(amount);
//		AuthorizationFactory
//				.getInstance(certificate, merchantDetails.getMerchantID(),
//						TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN)
//				.setStaging(true).setCaptureDetails(captureDTO).buildCapture();
//		System.out.println("Transaction Captured Successfully");
//	}
//
//	public static void transaction(List<String> items, String acquirer) throws ApplicationError, IOException {
//		Faker faker = new Faker();
//		AddressDTO addressDTO = new AddressDTO(faker.internet().emailAddress());
//		addressDTO.setCountry(faker.address().countryCode());
//		UrlDTO urlDetails = new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
//		CardDetailsDTO cardDetails = new CardDetailsDTO();
//		cardDetails.setCardNumber(TestConstants.VISA_CARD);
//		cardDetails.setCardType("");
//		AccountDTO accountDetails = new AccountDTO();
//		accountDetails.setBeneficiaryTypeCode(BeneficiaryTypeCodeEnum.PERSON);
//		cardDetails.setExpMonth("04");
//		cardDetails.setExpYear("2028");
//		cardDetails.setCardType(CardTypeEnum.VISA_CARD.getValue());
//		cardDetails.setCvv("123");
//		cardDetails.setNameOnCard(faker.name().fullName());
//		CustomerDTO customerDTO = new CustomerDTO();
//		customerDTO.setIpAddress(faker.internet().ipV4Address());
//		double amount = 10 + random.nextInt(50);
//		String txnReference = TestConstants.getUniqueRef("TEST");
//		TransactionDTO transactionDetails = new TransactionDTO(txnReference, BigDecimal.valueOf(amount), "EUR", false);
//		transactionDetails.setMidTag("twa-" + acquirer);
//		Map<String, Object> stringObjectMap = PaymentFactory
//				.getInstance(certificate, merchantDetails, TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN)
//				.setTransactionDetails(transactionDetails).setBillingDetails(addressDTO).setCardDetails(cardDetails)
//				.setCustomerDetails(customerDTO).setUrlDetails(urlDetails).buildPayment();
//		FormBuilderService formBuilder = new FormBuilderService();
//		formBuilder.createForm(stringObjectMap);
//		System.out.println("Preauth Successful.");
//		captureTxn(transactionDetails.getTxnReference(), transactionDetails.getCurrencyCode(),
//				transactionDetails.getTxnAmount().toString());
//		CsvBuilderService.prepareReconciliationFile(items, acquirer, String.valueOf(amount), txnReference,
//				LocalDateTime.now());
//	}
//
//	private static void curlRequest(FormBuilderService formBuilder, TransactionDTO transactionDetails) throws IOException {
//		String curlRequest;
//		// curlRequest =
//		// formBuilder.curlChargeBackHelpRequest(transactionDetails.getTxnAmount().toString());
//		curlRequest = formBuilder.nethoneCurlRequest(transactionDetails.getTxnAmount().toString(),
//				transactionDetails.getTxnReference());
//		// curlRequest =
//		// formBuilder.ethocaCurlRequest(transactionDetails.getTxnAmount().toString(),
//		// transactionDetails.getTxnReference());
//		System.out.println(curlRequest);
//		Process process = Runtime.getRuntime().exec("cmd /c clip");
//		try (OutputStream outputStream = process.getOutputStream()) {
//			outputStream.write(curlRequest.getBytes());
//		}
//	}
//
//	public static void getPaymentToken() throws ApplicationError {
//		MerchantDTO merchantDetails = new MerchantDTO(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID, "Customer_21123213");
//		Certificate certificate = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);
//		String ACCESS_TOKEN = "dc8c4655e89d44df936c7082f1fb4ab7";
//		DecryptPaymentTokenRequestDTO tokenRequestDTO = new DecryptPaymentTokenRequestDTO();
//		tokenRequestDTO.setMerchantID(merchantDetails.getMerchantID());
//		tokenRequestDTO.setPaymentToken(
//				"eyJzaWduYXR1cmUiOiJNRVFDSUR0a2FYcmV0NGdLdHB3Smp1bkRsbDFKb0tqWGZDdWZiMXNCMGRHUzNjQ0RBaUJ0MG03eGZiVzVtYVpsRVpONXQrazlnUXpyQ1dUR0g4SFJJZWNrQ0NSRlNnXHUwMDNkXHUwMDNkIiwiaW50ZXJtZWRpYXRlU2lnbmluZ0tleSI6eyJzaWduZWRLZXkiOiJ7XCJrZXlWYWx1ZVwiOlwiTUZrd0V3WUhLb1pJemowQ0FRWUlLb1pJemowREFRY0RRZ0FFZC9Ld3lOVnpaZGRQYjN0WDBJYjMxTEFjeUZLb3dXSGNGWTFiUWl2RWxNKzZrUWxZWllPejZzL2Z5WDFNeWNvVCtBZ2dycDRhYXhNMXRJREJmZyt0MlFcXHUwMDNkXFx1MDAzZFwiLFwia2V5RXhwaXJhdGlvblwiOlwiMTc1NDQ3NDAwMzAzN1wifSIsInNpZ25hdHVyZXMiOlsiTUVZQ0lRQ2V0cXpMSm9FQzVhaHJzZDNYZUMvSVJaNTVtKzhqRDdSajdVNXhzL0IxeGdJaEFMekNlamJrSzgvcTRic2Q2MDhzOCs5aVRsNTc5L0lPNDhKUmVraytmVnpmIl19LCJwcm90b2NvbFZlcnNpb24iOiJFQ3YyIiwic2lnbmVkTWVzc2FnZSI6IntcImVuY3J5cHRlZE1lc3NhZ2VcIjpcInZXRE56TmFicW9HalYwYjF4VHJMbWRqM3dDSmRUdUhjQjJSNmR3TmdEMlMwZ3Y3TTJDWmpXYkxGUEVTaElGalJGSzlZcVlXYzhrcHUvNVBtd1pOTTFPSHdicGd5UW0zcys5VTRhNnd2VmJ1Z1dMMWZwWkpPUTlaZlpHSUo1U0t0Y3ppMGtPOTB2OTRFNVF3bEI3TXRnU1VJL3JxS2M0UXY0Vzk3dEw3eEV1aXU3cGRjN1BvTENOT0l1ckpUelFHMWE5NURjT1E5RlN1aFdCYU1YVC9tUUx3WDZUZWNSYmx0eENqbXJVMXgyRVZPZXkwaSs2TVoxdExUQVBhd0VyZlJmMWs0L0Mva20vRysvVFhvTWNWbUk1NTh1Rmk5SkhvQ0owT053TDRLR0svMmw0NFV0L1BVU2w5VTBSa3FDOTJBb1hXVHdzMTlrR2dpTDJZVXpCVE5XTHByMWZCdGczZUFQZ200VWdIaStCVmhqL3dmaXJyR1Q4Szd0SmVCSHdRMVpiL1Y3QmhQT0x6cG1KZjZCL1RtbU00KzIvMVFjQU1FVzdTbk41Y2JGS3F0cmJ6N0NNTjVCSWdyRk5sYUlVd3lmVitpUHFOWWxSSk9rRkVYdjg0UGE1OEhzWFRMUTdDcEJVSFMvQVBZYmtQTkpKV2VGMENuR25BdlJ2RCsvU2Y4aUFpSDIvYVBtaXRjZkx0RHBmVUx3NFlZL0tBa3ZCd21xMkhNUmtGVGNYcGZQNXJ1WU9vXFx1MDAzZFwiLFwiZXBoZW1lcmFsUHVibGljS2V5XCI6XCJCREdhVVVDWGU3Z2tjSHFEMk16QjJET2ZWZVdFNGlIRVUvWDJaaVhxaG1NWDBOemJUemZMMlJCa1VKODdTN1FXUW9tUlNQWkpnL2JkM3IvOVdTN3BEL2NcXHUwMDNkXCIsXCJ0YWdcIjpcImxKUkNNc1QrTTIrMVdIRHpoM2RUaVM4OVU2L0RPZXhRdTRQU2plL05Db3NcXHUwMDNkXCJ9In0=");
//		tokenRequestDTO.setMerchantIdentifier("BCR2DN4TTCNKREAC");
//		tokenRequestDTO.setGatewayIdentifier("celerispay");
//		tokenRequestDTO.setPaymentMode(PaymentModeEnum.GOOGLE_PAY);
//		String paymentToken = PaymentFactory
//				.getInstance(certificate, merchantDetails, TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN)
//				.getPaymentToken(tokenRequestDTO);
//		System.out.println(paymentToken);
//	}
//
//	public static void oneStepSubscription() throws ApplicationError {
//		InstallmentsDTO installments1 = new InstallmentsDTO("day", 1, 14, "TRIAL", "1.85", "EUR");
//		InstallmentsDTO installments2 = new InstallmentsDTO("Month", 1, 12, "REGULAR", "5.65", "EUR");
//
//		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
//		CardDetailsDTO cardDetails = new CardDetailsDTO();
//		cardDetails.setCardNumber(TestConstants.VISA_CARD);
//		cardDetails.setExpMonth("04");
//		cardDetails.setExpYear("2028");
//		cardDetails.setCvv("123");
//		cardDetails.setNameOnCard(TestConstants.CARD_HOLDER_NAME);
//
//		UrlDTO urlDetails = new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
//
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(103),
//				"EUR");
//
//		PlanDTO plan = new PlanDTO("test-plan-", "mcb20241108" + random.nextInt(99999),
//				List.of(installments1, installments2));
//		Map<String, Object> finalResponse = PaymentFactory
//				.getInstance(certificate, merchantDetails, TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN).setPlan(plan)
//				.setTransactionDetails(transactionDetails).setUrlDetails(urlDetails).setCardDetails(cardDetails)
//				.setBillingDetails(addressDetails).buildPayment();
//		System.out.println("Final Response: " + finalResponse);
//		FormBuilderService formBuilder = new FormBuilderService();
//		formBuilder.createForm(finalResponse);
//	}
//
//	public static void payout() throws ApplicationError {
//		AddressDTO addressDTO = new AddressDTO("test@gmail.com");
//		addressDTO.setCountry("IN");
//		UrlDTO urlDetails = new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
//		CardDetailsDTO cardDetails = new CardDetailsDTO();
//		cardDetails.setCardNumber(TestConstants.VISA_CARD);
//		cardDetails.setExpMonth("04");
//		cardDetails.setExpYear("2028");
//		cardDetails.setCvv("123");
//		cardDetails.setNameOnCard(TestConstants.CARD_HOLDER_NAME);
//
//		double val = 10 + random.nextInt(10) + 0.22;
//		TransactionDTO transactionDetails = new TransactionDTO(TestConstants.getUniqueRef("TEST"),
//				BigDecimal.valueOf(val), "EUR", false);
//
//		Map<String, Object> stringObjectMap = PayoutFactory
//				.getInstance(certificate, merchantDetails, TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN).setStaging(true)
//				.setTransactionDetails(transactionDetails).setCardDetails(cardDetails)
//				.setPaymentMode(PaymentModeEnum.CREDIT_CARD).setUrlDetails(urlDetails).buildPayout();
//		System.out.println(stringObjectMap);
//		FormBuilderService formBuilder = new FormBuilderService();
//		formBuilder.createForm(stringObjectMap);
//	}
}
