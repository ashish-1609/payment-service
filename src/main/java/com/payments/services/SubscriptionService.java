package com.payments.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gateway.constants.enums.RecurringReasonEnum;
import com.gateway.constants.enums.RecurringSourceEnum;
import com.gateway.constants.enums.RecurringTypeEnum;
import com.gateway.dto.RecurringDTO;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.TransactionDTO;
import com.gateway.payment.UrlDTO;
import com.gateway.subscription.ScheduleUpdateDTO;
import com.gateway.subscription.SubscriptionFactory;
import com.payments.commons.TestConstants;

@Service
public class SubscriptionService {

	private static final String SUCCESS = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=success";
	private static final String FAIL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=fail";
	private static final String CANCEL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=cancel";
	private static final String ACCESS_TOKEN = TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN;
	private static final MerchantDTO MERCHANT_DETAILS = new MerchantDTO("TES250325001",
			TestConstants.getUniqueRef("TEST-CUS"));
	private static final Certificate CERTIFICATE = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);

	public String createSubscription(String planId) throws ApplicationError {
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
		addressDetails.setFirstName("");
		addressDetails.setLastName("");
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);

//		RecurringDTO recurringDTO = new RecurringDTO(RecurringSourceEnum.CIT, RecurringTypeEnum.RECURRING,
//				RecurringReasonEnum.REAUTHORIZATION);
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), true);
//		transactionDetails.setRecurring(recurringDTO);

		return (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setBillingDetails(addressDetails).setTransactionDetails(transactionDetails).setUrlDetails(urlDetails)
				.setStartDate(LocalDateTime.now().toString()).setQuantity(1).setPlanId(planId).setAutomaticDebit(true)
				.setTotalCycles(2).setExpireIn(15).createSubscription();
	}

	public Object getSubscriptionDetails(String subscriptionId) throws ApplicationError {
		return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.getSubscriptionDetails(subscriptionId);
	}

	public Object deactivateSubscription(String subscriptionId) throws ApplicationError {
		return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setCustomRedirection(false).deactivateSubscription(subscriptionId);
	}

	public String reactivateSubscription(String subscriptionId) throws ApplicationError {
		return (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.reactivateSubscription(subscriptionId);
	}

	public String schedulerUpdateSubscription(String planId, String subscriptionId) throws ApplicationError {
		ScheduleUpdateDTO scheduleUpdateDTO = new ScheduleUpdateDTO(planId, "2025-04-16", "16-04-2025", 1);
		return (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setScheduleUpdateDTO(scheduleUpdateDTO).scheduleUpdateSubscription(subscriptionId);
	}
}