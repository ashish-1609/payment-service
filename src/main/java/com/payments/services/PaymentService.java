package com.payments.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gateway.authorization.AuthorizationFactory;
import com.gateway.authorization.CaptureDTO;
import com.gateway.constants.enums.CardTypeEnum;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.CustomerDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.PaymentFactory;
import com.gateway.payment.TransactionDTO;
import com.gateway.payment.UrlDTO;
import com.payments.commons.TestConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {

	private final FormBuilderService formBuilder;
	private final CsvBuilderService csvBuilderService;

	String customerID = TestConstants.getUniqueRef("CUS-TEST");

	public void payment(int count, String acquirer, boolean capture, boolean reconciliation, boolean subscription,
			boolean staging) {
		List<String> items = new ArrayList<>();
		if (count == 0) {
			count = 1;
		}
		if (!StringUtils.hasLength(acquirer)) {
			acquirer = "truevo";
		}
		try {
			for (int i = 0; i < count; i++) {
				processPayment(items, acquirer, capture, reconciliation, subscription, staging);
				log.info("{}.) Transaction Successful.", i+1);
			}
			if (reconciliation) {
				csvBuilderService.writeDataToFile(csvBuilderService.getFilePath(acquirer),
						csvBuilderService.updateListToCsv(items, acquirer));
			}
			log.info("Processing Completed Successfully");
		} catch (ApplicationError e) {
			log.error("Error occurred");
		}
	}

	public void processPayment(List<String> items, String acquirer, boolean capture, boolean reconciliation,
			boolean subscription, boolean staging) throws ApplicationError {
		Faker faker = new Faker();
		Random random = new Random();
		double amount = 10 + random.nextInt(50);
		String txnReference = TestConstants.getUniqueRef("TEST");
		TransactionDTO transactionDetails = new TransactionDTO(txnReference, BigDecimal.valueOf(amount), "EUR", false);
		transactionDetails.setMidTag("twa-" + acquirer);
		log.info("Payment Request: {}", transactionDetails);
		String accessToken = staging
				? TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN
				: TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN;
		Map<String, Object> payment = PaymentFactory.getInstance(getCertificate(), getMerchantDetails(), accessToken)
				.setStaging(staging).setTransactionDetails(transactionDetails)
				.setBillingDetails(getAddressDetails(faker)).setCardDetails(getCardDetails(faker))
				.setCustomerDetails(getCustomerDetails(faker)).setUrlDetails(getUrlDetails()).buildPayment();
		formBuilder.createForm(payment);
		if (!capture) {
			return;
		}
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			log.error("Error occurred while sleeping");
			return;
		}
		captureTxn(txnReference, "EUR", String.valueOf(amount), staging);
		if (reconciliation) {
			csvBuilderService.prepareReconciliationFile(items, acquirer, String.valueOf(amount), txnReference);
		}
	}

	public void captureTxn(String txnRef, String currency, String amount, boolean staging) throws ApplicationError {
		CaptureDTO captureDTO = new CaptureDTO(txnRef, currency);
		captureDTO.setAmount(amount);
		String accessToken = staging
				? TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN
				: TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN;
		AuthorizationFactory.getInstance(getCertificate(), getMerchantDetails().getMerchantID(), accessToken)
				.setStaging(staging).setCaptureDetails(captureDTO).buildCapture();
		log.info("Transaction Captured Successfully");
	}

	private CustomerDTO getCustomerDetails(Faker faker) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setIpAddress(faker.internet().ipV4Address());
		return customerDTO;
	}

	private AddressDTO getAddressDetails(Faker faker) {
		AddressDTO addressDetails = new AddressDTO(faker.internet().emailAddress());
		addressDetails.setCountry(faker.address().countryCode());
		return addressDetails;
	}

	private CardDetailsDTO getCardDetails(Faker faker) {
		CardDetailsDTO cardDetails = new CardDetailsDTO();
		cardDetails.setCardNumber(TestConstants.VISA_CARD);
		cardDetails.setCardType("");
		cardDetails.setExpMonth("04");
		cardDetails.setExpYear("2028");
		cardDetails.setCardType(CardTypeEnum.VISA_CARD.getValue());
		cardDetails.setCvv("123");
		cardDetails.setNameOnCard(faker.name().fullName());
		return cardDetails;
	}

	private UrlDTO getUrlDetails() {
		return new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
	}

	private MerchantDTO getMerchantDetails() {
		return new MerchantDTO(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID, "CUSTEST202511051951139");
	}

	private Certificate getCertificate() {
		return new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);
	}

}
