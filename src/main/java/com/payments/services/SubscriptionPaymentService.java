package com.payments.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gateway.constants.enums.PaymentModeEnum;
import com.gateway.constants.enums.RecurringReasonEnum;
import com.gateway.constants.enums.RecurringSourceEnum;
import com.gateway.constants.enums.RecurringTypeEnum;
import com.gateway.dto.RecurringDTO;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.CustomDataDTO;
import com.gateway.payment.DiscountDTO;
import com.gateway.payment.DynamicDescriptorDTO;
import com.gateway.payment.ItemDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.PaymentFactory;
import com.gateway.payment.SummaryDTO;
import com.gateway.payment.SummaryDetailsDTO;
import com.gateway.payment.TokenDTO;
import com.gateway.payment.TransactionDTO;
import com.gateway.payment.UrlDTO;
import com.payments.commons.TestConstants;

@Service
public class SubscriptionPaymentService {

	private static final String SUCCESS = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=success";
	private static final String FAIL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=fail";
	private static final String CANCEL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=cancel";
	private static final String ACCESS_TOKEN = TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN;
	private static final MerchantDTO MERCHANT_DETAILS = new MerchantDTO("TES250325001",
			TestConstants.getUniqueRef("TEST-CUS"));
	private static final Certificate CERTIFICATE = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);

	private final Random random = new Random();

	public Map<String, Object> createStandardTransaction(String subscriptionId) throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		CardDetailsDTO cardDetails = new CardDetailsDTO();
		cardDetails.setCardNumber("4200000000000000");
		cardDetails.setExpMonth("04");
		cardDetails.setExpYear("2028");
		cardDetails.setCvv("123");
		cardDetails.setSaveDetails(true);
		cardDetails.setNameOnCard("John Smith");

		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(),
				BigDecimal.valueOf(random.nextInt(30) + .99), "EUR");
		transactionDetails.setSubscriptionId(subscriptionId);

		return PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setTransactionDetails(transactionDetails).setUrlDetails(urlDetails).setCardDetails(cardDetails)
				.buildPayment();
	}

	public Map<String, Object> initialPaymentCreatePayment(String subscriptionId) throws ApplicationError {
		AddressDTO billingDetails = new AddressDTO("john@gmail.com");
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.ONE, "EUR");
		transactionDetails.setSubscriptionId(subscriptionId);

		return PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setBillingDetails(billingDetails).setUrlDetails(urlDetails).setTransactionDetails(transactionDetails)
				.buildPayment();
	}

	public Map<String, Object> initialSubscriptionCreateExpressPaymentWithCard(String subscriptionId)
			throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.TEN, "EUR");
		transactionDetails.setSubscriptionId(subscriptionId);
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");

		DynamicDescriptorDTO descriptor = new DynamicDescriptorDTO("John Doe");
		descriptor.setEmail("john@gmail.com");
		descriptor.setMobile("123456789");
		CardDetailsDTO cardDetails = new CardDetailsDTO();
		cardDetails.setCardNumber("4200000000000000");
		cardDetails.setExpMonth("04");
		cardDetails.setExpYear("2028");
		cardDetails.setCvv("123");
		cardDetails.setNameOnCard("John Smith");

		List<ItemDTO> itemDetails = new ArrayList<>();
		itemDetails.add(new ItemDTO(String.valueOf(random.nextInt(11111111)), "Item 1", 1, BigDecimal.ONE));
		SummaryDetailsDTO summaryDetailsDTO = new SummaryDetailsDTO("1", "1", "1");
		DiscountDTO discountDTO = new DiscountDTO("", "", "");
		SummaryDTO summaryDetails = new SummaryDTO(summaryDetailsDTO, discountDTO, "1");
		CustomDataDTO customDataDetails = new CustomDataDTO("", "", "", "", "", "");

//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.RECURRING,
//				RecurringReasonEnum.REAUTHORIZATION);
//		transactionDetails.setRecurring(recurring);

		return PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setTransactionDetails(transactionDetails).setBillingDetails(addressDetails).setUrlDetails(urlDetails)
				.setDynamicDescriptor(descriptor).setItemDetails(itemDetails).setSummaryDetails(summaryDetails)
				.setCustomDataDetails(customDataDetails).setPaymentMode(PaymentModeEnum.CREDIT_CARD).buildPayment();
	}

	public Map<String, Object> subscriptionSubsequentRebillingWithoutCVV(String subscriptionId)
			throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1.9),
				"EUR");
		transactionDetails.setSubscriptionId(subscriptionId);

//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.UNSCHEDULED,
//				RecurringReasonEnum.NOT_SHOW);
//		transactionDetails.setRecurring(recurring);
		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000");

		return PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setUrlDetails(urlDetails).setBillingDetails(addressDetails).setTransactionDetails(transactionDetails)
				.setTokenDetails(tokenDetails).buildPayment();
	}

	public Map<String, Object> subscriptionSubsequentRebillingWithCVV(String subscriptionId) throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1),
				"EUR", PaymentModeEnum.CREDIT_CARD);
		transactionDetails.setSubscriptionId(subscriptionId);

//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.UNSCHEDULED,
//				RecurringReasonEnum.NOT_SHOW);
//		transactionDetails.setRecurring(recurring);
		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000", "123", true);

		return PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setUrlDetails(urlDetails).setBillingDetails(addressDetails).setTransactionDetails(transactionDetails)
				.setTokenDetails(tokenDetails).buildPayment();
	}
}