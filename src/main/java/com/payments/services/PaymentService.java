package com.payments.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.gateway.constants.enums.PaymentModeEnum;
import com.payments.commons.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gateway.authorization.AuthorizationFactory;
import com.gateway.authorization.CaptureDTO;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.PaymentFactory;
import com.gateway.payment.TransactionDTO;
import com.payments.beans.PaymentRequest;
import com.payments.commons.TestConstants;
//import com.payments.commons.Utils;
import com.payments.enums.Environment;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {

	private final FormBuilderService formBuilder;
	private final CsvBuilderService csvBuilderService;
	private final Random random = new Random();

	public void payment(PaymentRequest paymentRequest) {
		if (paymentRequest == null) {
			return;
		}

		int count = paymentRequest.getCount() == 0 ? 1 : paymentRequest.getCount();
		List<String> items = Collections.synchronizedList(new ArrayList<>());
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		try {
			List<Future<?>> futures = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				futures.add(executorService.submit(() -> {
					try {
						PaymentRequest requestCopy = new PaymentRequest();
						BeanUtils.copyProperties(paymentRequest, requestCopy);
						String acquirer = requestCopy.getAcquirer();
						if (!StringUtils.hasLength(acquirer)) {
							acquirer = TestConstants.MID_TAGS.get(random.nextInt(TestConstants.MID_TAGS.size()));
							requestCopy.setAcquirer(acquirer);
						}
						log.info("Processing Transaction using {} Thread", Thread.currentThread().getName());
						log.info("Acquirer Obtained for transaction: {}", acquirer);
						processPayment(items, requestCopy);
					} catch (ApplicationError e) {
						log.error("Error occurred: {}", e.getMessage(), e);
					}
				}));
				log.info("{}.) Transaction Submitted.", i + 1);
			}
			for (Future<?> future : futures) {
				future.get();
			}
			String acquirer = paymentRequest.getAcquirer();
			if (paymentRequest.isReconciliation()) {
				csvBuilderService.writeDataToFile(
						csvBuilderService.getFilePath(acquirer, paymentRequest.getReconciliationType()),
						csvBuilderService.updateListToCsv(items, acquirer));
			}
			log.info("Processing Completed Successfully");
		} catch (Exception e) {
			log.error("Unexpected error: {}", e.getMessage(), e);
		} finally {
			executorService.shutdown();
		}
	}

	public void processPayment(List<String> items, PaymentRequest paymentRequest) throws ApplicationError {
		updateCredentials(paymentRequest);
		Random random = new Random();
		double amount = 10 + random.nextInt(50);
		String txnReference = TestConstants.getUniqueRef("TEST") + TestConstants.generateAuthCode();
		TransactionDTO transactionDetails = new TransactionDTO(txnReference, BigDecimal.valueOf(amount), "EUR", false);
		transactionDetails.setMidTag(paymentRequest.getAcquirer());
//		transactionDetails.setPaymentMode(PaymentModeEnum.CREDIT_CARD);
		if (paymentRequest.isSubscription()) {
			transactionDetails.setSubscriptionId(paymentRequest.getSubscriptionRequest().getSubscriptionId());
		}
		log.info("Payment Request: {}", transactionDetails);
		log.info("Merchant id: {}, accessToken: {}", paymentRequest.getMerchantId(), paymentRequest.getAccessToken());
		CardDetailsDTO cardDetails = Utils.getCardDetails(paymentRequest);
		String cardType = cardDetails.getCardType();
		Map<String, Object> payment = PaymentFactory
				.getInstance(getCertificate(paymentRequest), getMerchantDetails(paymentRequest),
						paymentRequest.getAccessToken())
				.setPaymentMode(PaymentModeEnum.CREDIT_CARD)
				.setDebugLogs(true).setStaging(paymentRequest.isStaging()).setTransactionDetails(transactionDetails)
				.setBillingDetails(Utils.getAddressDetails()).setCardDetails(cardDetails)
				.setCustomerDetails(Utils.getCustomerDetails()).setUrlDetails(Utils.getUrlDetails()).buildPayment();
		formBuilder.createForm(payment);
		if (paymentRequest.isCaptureTxn()) {
			sleep();
			captureTxn(txnReference, "EUR", String.valueOf(amount), paymentRequest);
		}
		if (paymentRequest.isReconciliation()) {
			csvBuilderService.prepareReconciliationFile(items, paymentRequest, String.valueOf(amount), txnReference,
					cardType);
		}
	}

	private static void sleep() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Error occurred while sleeping");
		}
	}

	public void captureTxn(String txnRef, String currency, String amount, PaymentRequest paymentRequest)
			throws ApplicationError {
		CaptureDTO captureDTO = new CaptureDTO(txnRef, currency);
		captureDTO.setAmount(amount);
		AuthorizationFactory
				.getInstance(getCertificate(paymentRequest), getMerchantDetails(paymentRequest).getMerchantID(),
						paymentRequest.getAccessToken())
				.setStaging(paymentRequest.isStaging()).setCaptureDetails(captureDTO).buildCapture();
		log.info("Transaction Captured Successfully");
	}

	private MerchantDTO getMerchantDetails(PaymentRequest paymentRequest) {
		return new MerchantDTO(paymentRequest.getMerchantId(), TestConstants.getUniqueRef("CUS-TEST"));
	}

	private Certificate getCertificate(PaymentRequest paymentRequest) {
		return new Certificate(paymentRequest.getCertPath(), null);
	}

	private void updateCredentials(PaymentRequest paymentRequest) {
		updateEnvironment(paymentRequest);
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID)
				&& paymentRequest.isStaging()) {
			paymentRequest.setAccessToken(TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN);
//			 paymentRequest.setAccessToken("872bdee113524d8f8450df57cf326942");
			paymentRequest.setCertPath(TestConstants.TEST_WITH_ASHISH_CERT_PATH);
			return;
		}
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.WOOD_MERCHANT_MERCHANT_ID)) {
			paymentRequest.setAccessToken(TestConstants.WOOD_MERCHANT_ACCESS_TOKEN);
			paymentRequest.setCertPath(TestConstants.WOOD_MERCHANT_CERT_PATH);
			return;
		}
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID)) {
			paymentRequest.setAccessToken(TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN);
			paymentRequest.setCertPath(TestConstants.TEST_WITH_ASHISH_CERT_PATH);
			return;
		}
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.QA_TEST_MERCHANT_MERCHANT_ID)) {
			paymentRequest.setAccessToken(TestConstants.QA_TEST_MERCHANT_ACCESS_TOKEN);
			paymentRequest.setCertPath(TestConstants.QA_TEST_MERCHANT_CERT_PATH);
		}
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.TEST_ALGO_GRANDCHILD_1_MERCHANT_ID)) {
			paymentRequest.setAccessToken(TestConstants.TEST_ALGO_GRANDCHILD_1_ACCESS_TOKEN);
			paymentRequest.setCertPath(TestConstants.TEST_ALGO_GRANDCHILD_1_CERT_PATH);
		}
		if (paymentRequest.getMerchantId().equalsIgnoreCase(TestConstants.TEST_ALGO_GRANDCHILD_3_MERCHANT_ID)) {
			paymentRequest.setAccessToken(TestConstants.TEST_ALGO_GRANDCHILD_3_ACCESS_TOKEN);
			paymentRequest.setCertPath(TestConstants.TEST_ALGO_GRANDCHILD_3_CERT_PATH);
		}
	}

	private void updateEnvironment(PaymentRequest paymentRequest) {
		if (paymentRequest.getEnvironment().equalsIgnoreCase(Environment.LOCAL.getValue())) {
			paymentRequest.setStaging(true);
		}
		if (paymentRequest.getEnvironment().equalsIgnoreCase(Environment.DEV.getValue())) {
			paymentRequest.setStaging(false);
		}
		if (paymentRequest.getEnvironment().equalsIgnoreCase(Environment.STAG.getValue())) {
			paymentRequest.setStaging(false);
		}
		if (paymentRequest.getEnvironment().equalsIgnoreCase(Environment.SUSHIL_SIR.getValue())) {
			paymentRequest.setStaging(false);
		}
	}

}
