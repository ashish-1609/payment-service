//package com.payments.controllers;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import com.gateway.constants.enums.NotificationChannelEnum;
//import com.gateway.encryption.Certificate;
//import com.gateway.exception.ApplicationError;
//import com.gateway.payment.BillingDTO;
//import com.gateway.payment.CustomerDTO;
//import com.gateway.payment.MerchantDTO;
//import com.gateway.payment.NotificationChannels;
//import com.gateway.payment.PaymentLinkFactory;
//import com.gateway.payment.TransactionDTO;
//import com.payments.commons.TestConstants;
////import com.payments.commons.Utils;
//
//import net.datafaker.Faker;
//
//@RestController
//public class PaymentLinkController {
//
//	public void createPaymentLink(String acquirer, boolean staging) throws ApplicationError {
//		Faker faker = new Faker();
//		Random random = new Random();
//		double amount = 10 + random.nextInt(50);
////		CustomerDTO customerDetails = Utils.getCustomerDetails();
////		BillingDTO billingDetails = customerDetails.getBillingAddress();
//		String txnReference = TestConstants.getUniqueRef("TEST");
//		TransactionDTO transactionDetails = new TransactionDTO(txnReference, BigDecimal.valueOf(amount), "EUR", false);
//		transactionDetails.setMidTag(acquirer);
//		transactionDetails.setCustomerEmail(billingDetails.getEmailId());
//		transactionDetails.setPaymentLinkDescription("Payment for " + faker.pokemon().name() + "toy.");
//		transactionDetails.setAllow3D(false);
//		transactionDetails.setSendSMS(true);
//		transactionDetails.setAllowBillShip(true);
//
//		List<NotificationChannels> notificationChannels = new ArrayList<>();
//		notificationChannels.add(new NotificationChannels(NotificationChannelEnum.EMAIL, billingDetails.getEmailId()));
//		notificationChannels.add(new NotificationChannels(NotificationChannelEnum.SLACK, ""));
//		notificationChannels.add(new NotificationChannels(NotificationChannelEnum.SMS, billingDetails.getMobileNo()));
//		notificationChannels
//				.add(new NotificationChannels(NotificationChannelEnum.WHATSAPP, billingDetails.getMobileNo()));
//
//		Object generateLink = PaymentLinkFactory
//				.getInstance(getCertificate(), getMerchantDetails(), Utils.getAccessToken(staging)).setStaging(true)
//				.setTransactionDetails(transactionDetails).setUrlDetails(Utils.getUrlDetails())
//				.setNotificationChannel(notificationChannels).setDynamicDescriptor(Utils.getDynamicDescriptor())
//				.generateLink();
//		System.out.println(generateLink);
//
//	}
//
//	private MerchantDTO getMerchantDetails() {
//		return new MerchantDTO(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID, TestConstants.getUniqueRef("CUS-TEST"));
//	}
//
//	private Certificate getCertificate() {
//		return new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);
//	}
//
//}
