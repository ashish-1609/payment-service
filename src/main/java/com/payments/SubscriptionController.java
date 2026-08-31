package com.payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import com.gateway.constants.enums.AdjustmentTypeEnum;
import com.gateway.constants.enums.NotificationChannelEnum;
import com.gateway.constants.enums.PaymentModeEnum;
import com.gateway.constants.enums.PeriodEnum;
import com.gateway.constants.enums.RecurringReasonEnum;
import com.gateway.constants.enums.RecurringSourceEnum;
import com.gateway.constants.enums.RecurringTypeEnum;
import com.gateway.dto.RecurringDTO;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.BillingDTO;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.CustomDataDTO;
import com.gateway.payment.CustomerDTO;
import com.gateway.payment.DiscountDTO;
import com.gateway.payment.DynamicDescriptorDTO;
import com.gateway.payment.ItemDTO;
import com.gateway.payment.MerchantDTO;
import com.gateway.payment.NotificationChannels;
import com.gateway.payment.PaymentDetailsDTO;
import com.gateway.payment.PaymentFactory;
import com.gateway.payment.ShippingDTO;
import com.gateway.payment.SummaryDTO;
import com.gateway.payment.SummaryDetailsDTO;
import com.gateway.payment.TokenDTO;
import com.gateway.payment.TransactionDTO;
import com.gateway.payment.UrlDTO;
import com.gateway.subscription.ConditionDTO;
import com.gateway.subscription.InstallmentsDTO;
import com.gateway.subscription.PlanDTO;
import com.gateway.subscription.RetryDTO;
import com.gateway.subscription.ScheduleUpdateDTO;
import com.gateway.subscription.SubscriptionFactory;
import com.payments.commons.TestConstants;
//import com.payments.commons.Utils;
import com.payments.services.FormBuilderService;
import net.datafaker.Faker;

public class SubscriptionController {

	public static final String SUCCESS = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String FAIL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String CANCEL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String ACCESS_TOKEN = TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN;
	public static final Random random = new Random();
	public static final MerchantDTO MERCHANT_DETAILS = new MerchantDTO(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID,
			"CUSTOMER-2026-05-08");
	public static final Certificate CERTIFICATE = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);
	public static final String PLAN_ID = "2bab242c-17fe-47cd-865f-709cbb263f4b";
	// public static final String PLAN_ID = "30c214ef-6f30-443a-a342-4d411c496c39";
	private static final String SUBSCRIPTION_ID = "a7725cc7-5ee2-4118-94e1-ca0db8653a2c";
	private static final Boolean STAGING = true;

	public static void main(String[] args) throws ApplicationError {
		upsellTxn();
//		while (true) {
//			Scanner scanner = new Scanner(System.in);
//			printLine();
//			System.out.println("Subscription");
//			printLine();
//			System.out.println("What do you want to do?");
//			System.out.println("""
//
//					    1.     Create Plan
//					    2.     Create Subscription\s
//					    \
//					3.     Deactivate Subscription\s
//					    4.     Reactivate Subscription
//					    \
//					5.     Get Subscription Details\s
//					    6.     Create Rule With Mac Code\s
//					    \
//					7.     Create Rule Without Mac Code\s
//					    8.     get Rule\s
//					    \
//					9.     Initial Subscription-Create Payment\s
//					    10.    Update Recovery Rule\s
//					    \
//					11.    Get Recovery Rule\s
//					    \
//					12.    Subscription Subsequent Re-billing Without CVV  \s
//					    \
//					13.    Subscription Subsequent Re-billing With CVV   \s
//					    \
//					14.    Initial Subscription create Express Payment With Card\s
//					    \
//					15.    Scheduler Update Subscription\s
//					   \s
//					""");
//			printLine();
//			System.out.print("Enter your choice: ");
//			int choice = scanner.nextInt();
//			printLine();
//			switch (choice) {
//				case 0 :
//					transaction();
//					break;
//				case 1 :
//					createPlan();
//					break;
//				case 2 :
//					createSubscription();
//					break;
//				case 3 :
//					deactivateSubscription();
//					break;
//				case 4 :
//					reactivateSubscription();
//					break;
//				case 5 :
//					getSubscriptionDetails();
//					break;
//				case 6 :
//					createRuleWithMacCode();
//					break;
//				case 7 :
//					createRuleWithoutMacCode();
//					break;
//				case 8 :
//					getRule();
//					break;
//				case 9 :
//					initialPaymentCreatePayment();
//					break;
//				case 10 :
//					updateRecoveryRule();
//					break;
//				case 11 :
//					getRecoveryRule();
//					break;
//				case 12 :
//					subscriptionSubsequentRebillingWithoutCVV();
//					break;
//				case 13 :
//					subscriptionSubsequentRebillingWithCVV();
//					break;
//				case 14 :
//					initialSubscriptionCreateExpressPaymentWithCard();
//				case 15 :
//					schedulerUpdateSubscription();
//					break;
//				default :
//					break;
//			}
//		}

	}

	public static String certPath = TestConstants.QA_TEST_MERCHANT_CERT_PATH;
	public static String merchantId = TestConstants.QA_TEST_MERCHANT_MERCHANT_ID;
	public static String accessToken = TestConstants.QA_TEST_MERCHANT_ACCESS_TOKEN;

	static Certificate certificate = new Certificate(certPath, null);
	static MerchantDTO merchantDetails;
	static {
		String customerID = TestConstants.getUniqueRef("CUS-TEST");
		merchantDetails = new MerchantDTO(merchantId, "USR33300012");
	}

	public static void upsellTxn() throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
		urlDetails.setIFrame(false);
		Faker faker = new Faker();
		AddressDTO addressDTO = new AddressDTO(faker.internet().emailAddress());
		addressDTO.setCountry("BR");
		addressDTO.setFirstName(faker.name().firstName());
		addressDTO.setLastName(faker.name().lastName());

		CardDetailsDTO cardDetails = new CardDetailsDTO();
		cardDetails.setCvv("000");
		cardDetails.setCardNumber("4111111111111111");
		cardDetails.setExpMonth("12");
		cardDetails.setSaveDetails(true);
		cardDetails.setExpYear("2030");
		cardDetails.setNameOnCard(faker.name().nameWithMiddle());
		//
		PaymentDetailsDTO paymentDetails = new PaymentDetailsDTO();
		paymentDetails.setSaveDetails(true);
		paymentDetails.setCheckTokenStatus(false);
		paymentDetails.setTokenID("411111-467603803837712036-1111");
		//
		// paymentDetails.setCheckTokenStatus(Boolean.FALSE);
		// paymentDetails.setTokenID("411111-230158421150184565-1111");

		CustomerDTO customerDTO = new CustomerDTO(new BillingDTO(addressDTO), new ShippingDTO(addressDTO));

		RecurringDTO recurring = new RecurringDTO();
		recurring.setUpsell(true);
		recurring.setSource(RecurringSourceEnum.MIT.getValue());
		recurring.setRecurringType(RecurringTypeEnum.RECURRING.getValue());
		recurring.setReason(RecurringReasonEnum.NOT_SHOW.getValue());
		recurring.setUpsellReference("Test202607222037068");
		String test = TestConstants.getUniqueRef("Test");
		System.out.println(test);
		TransactionDTO transaction = new TransactionDTO(test, new BigDecimal("0.1"), "EUR", true);
		transaction.setMidTag("test-payxpert");
		transaction.setRecurring(recurring);

		PaymentFactory paymentFactory = PaymentFactory
				.getInstance(new Certificate(certPath, null), merchantDetails, accessToken).setStaging(STAGING)
				.setUrlDetails(urlDetails).setCustomerDetails(customerDTO).setPaymentMode(PaymentModeEnum.CREDIT_CARD)
				// .setCardDetails(cardDetails)
				.setPaymentDetails(paymentDetails).setBillingDetails(addressDTO).setTransactionDetails(transaction);
		//
		// paymentFactory.paymentDetails.setSaveDetails(true);
		Map<String, Object> payment = paymentFactory.buildPayment();
		FormBuilderService builder = new FormBuilderService();
		builder.createForm(payment);
	}}

//	private static void transaction() throws ApplicationError {
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		CardDetailsDTO cardDetails = new CardDetailsDTO();
//		cardDetails.setCardNumber("4200000000000000");
//		cardDetails.setExpMonth("04");
//		cardDetails.setExpYear("2028");
//		cardDetails.setCvv("123");
//		cardDetails.setSaveDetails(true);
//		cardDetails.setNameOnCard("John Smith");
//
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(),
//				BigDecimal.valueOf(new Random().nextInt(30) + .99), "EUR");
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//
//		Map<String, Object> stringObjectMap = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setTransactionDetails(transactionDetails).setUrlDetails(urlDetails)
//				.setCardDetails(cardDetails).buildPayment();
//		System.out.println(stringObjectMap);
//		FormBuilderService FormBuilderService = new FormBuilderService();
//		FormBuilderService.createForm(stringObjectMap);
//	}
//
//	private static void initialSubscriptionCreateExpressPaymentWithCard() throws ApplicationError {
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.TEN, "EUR");
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
//
//		DynamicDescriptorDTO descriptor = new DynamicDescriptorDTO("John Doe");
//		descriptor.setEmail("john@gmail.com");
//		descriptor.setMobile("123456789");
//		CardDetailsDTO cardDetails = new CardDetailsDTO();
//		cardDetails.setCardNumber("4200000000000000");
//		cardDetails.setExpMonth("04");
//		cardDetails.setExpYear("2028");
//		cardDetails.setCvv("123");
//		cardDetails.setNameOnCard("John Smith");
//
//		// Additional details for transaction (Optional)
//		List<ItemDTO> itemDetails = new ArrayList<>();
//		itemDetails.add(new ItemDTO(String.valueOf(new Random().nextInt(11111111)), "Item 1", 1, BigDecimal.ONE));
//		SummaryDetailsDTO summaryDetailsDTO = new SummaryDetailsDTO("1", "1", "1");
//		DiscountDTO discountDTO = new DiscountDTO("", "", "");
//		SummaryDTO summaryDetails = new SummaryDTO(summaryDetailsDTO, discountDTO, "1");
//		CustomDataDTO customDataDetails = new CustomDataDTO("", "", "", "", "", "");
//
//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.RECURRING,
//				RecurringReasonEnum.REAUTHORIZATION);
//		transactionDetails.setRecurring(recurring);
//
//		Map<String, Object> finalResponse = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setTransactionDetails(transactionDetails).setBillingDetails(addressDetails)
//				.setUrlDetails(urlDetails).setDynamicDescriptor(descriptor).setItemDetails(itemDetails)
//				.setSummaryDetails(summaryDetails).setCustomDataDetails(customDataDetails)
//				// .setCardDetails(cardDetails)
//				.setPaymentMode(PaymentModeEnum.CREDIT_CARD)
//				// .setDeviceFingerprint(deviceFingerprint)
//				// .setExemptions(exemptions)
//				// .setSDKDetails(threeDSdk)
//				// .setChallengeConfig(ChallengeIndicatorEnum.<CHALLENGE_INDICATOR>,
//				// ChallengeWindowEnum.<CHALLENGE_WINDOW>)
//				.buildPayment();
//		System.out.println(finalResponse);
//		FormBuilderService FormBuilderService = new FormBuilderService();
//		FormBuilderService.createForm(finalResponse);
//
//	}
//
//	private static void subscriptionSubsequentRebillingWithoutCVV() throws ApplicationError {
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1.9),
//				"EUR");
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//
//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.UNSCHEDULED,
//				RecurringReasonEnum.NOT_SHOW);
//		transactionDetails.setRecurring(recurring);
//
//		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000");
//
//		Map<String, Object> response = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setUrlDetails(urlDetails).setBillingDetails(addressDetails)
//				.setTransactionDetails(transactionDetails).setTokenDetails(tokenDetails).buildPayment();
//		System.out.println(response);
//		FormBuilderService FormBuilderService = new FormBuilderService();
//		FormBuilderService.createForm(response);
//	}
//
//	private static void subscriptionSubsequentRebillingWithCVV() throws ApplicationError {
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
//
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1),
//				"EUR", PaymentModeEnum.CREDIT_CARD);
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//
//		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.UNSCHEDULED,
//				RecurringReasonEnum.NOT_SHOW);
//		transactionDetails.setRecurring(recurring);
//
//		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000", "123", true);
//
//		Map<String, Object> response = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setUrlDetails(urlDetails).setBillingDetails(addressDetails)
//				.setTransactionDetails(transactionDetails).setTokenDetails(tokenDetails).buildPayment();
//		System.out.println(response);
//		FormBuilderService FormBuilderService = new FormBuilderService();
//		FormBuilderService.createForm(response);
//	}
//
//	private static void getRecoveryRule() throws ApplicationError {
//		String subscriptionPlan = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).getSubscriptionPlan(PLAN_ID);
//		System.out.println(subscriptionPlan);
//	}
//
//	private static void initialPaymentCreatePayment() throws ApplicationError {
//		AddressDTO billingDetails = new AddressDTO("john@gmail.com");
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.ONE, "EUR");
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//		Map<String, Object> payment = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setBillingDetails(billingDetails).setUrlDetails(urlDetails)
//				.setTransactionDetails(transactionDetails).buildPayment();
//		FormBuilderService FormBuilderService = new FormBuilderService();
//		FormBuilderService.createForm(payment);
//		System.out.println(payment);
//	}
//
//	public static void createRuleWithMacCode() throws ApplicationError {
//		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR");
//		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR");
//		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
//		RetryDTO retryDTO = new RetryDTO(1, 1, PeriodEnum.DAYS);
//
//		retryDTO.setConditions(List.of(new ConditionDTO("25")));
//
//		retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
//		PlanDTO planDetails = new PlanDTO("Recovery Rules", UUID.randomUUID().toString(), installments);
//		planDetails.setRetry(List.of(retryDTO));
//		System.out.println(planDetails);
//		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(STAGING)
//				.setPlanDetails(planDetails).createPlan();
//		System.out.println(plan);
//	}
//
//	public static void createRuleWithoutMacCode() throws ApplicationError {
//		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR");
//		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR");
//		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
//		RetryDTO retry = new RetryDTO(1, 1, PeriodEnum.DAY_OF_WEEK, 5);
//		retry.setTime("22:00");
//		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
//		RetryDTO retry1 = new RetryDTO(2, 1, PeriodEnum.DAY_OF_WEEK, 5);
//		retry.setTime("22:30");
//		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
//		RetryDTO retry2 = new RetryDTO(3, 8, PeriodEnum.DAY_OF_WEEK, 5);
//		retry.setTime("23:00");
//		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
//		PlanDTO planDetails = new PlanDTO("Test plan", UUID.randomUUID().toString(), installments);
//		planDetails.setRetry(List.of(retry, retry1, retry2));
//		System.out.println(planDetails);
//		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(STAGING)
//				.setPlanDetails(planDetails).createPlan();
//		System.out.println(plan);
//	}
//
//	private static void updateRecoveryRule() throws ApplicationError {
//		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 6, "regular", "10.00", "EUR");
//		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 3, "regular", "20.00", "EUR");
//		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
//		RetryDTO retryDTO = new RetryDTO(1, 2, PeriodEnum.DAYS);
//
//		retryDTO.setConditions(List.of(new ConditionDTO("25")));
//
//		retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 2);
//		PlanDTO planDetails = new PlanDTO("Recovery Rules Updated", installments);
//		planDetails.setRetry(List.of(retryDTO));
//		System.out.println(planDetails);
//		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(STAGING)
//				.setPlanDetails(planDetails).updateSubscriptionPlan(PLAN_ID);
//		System.out.println(plan);
//	}
//
//	private static void getRule() throws ApplicationError {
//		Object subscriptionPlan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.getSubscriptionPlan("6aae4ead-74e1-4c50-8ebb-b3a1e8785bdc");
//		System.out.println(subscriptionPlan);
//	}
//
//	private static void getSubscriptionDetails() throws ApplicationError {
//		Object subscriptionDetails = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).getSubscriptionDetails(SUBSCRIPTION_ID);
//		System.out.println(subscriptionDetails);
//	}
//
//	public static void deactivateSubscription() throws ApplicationError {
//		Object object = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(STAGING)
//				.deactivateSubscription(SUBSCRIPTION_ID);
//		System.out.println(object);
//	}
//
//	public static void reactivateSubscription() throws ApplicationError {
//		String object = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).reactivateSubscription(SUBSCRIPTION_ID);
//
//		System.out.println(object);
//	}
//
//	public static void createPlan() throws ApplicationError {
//		List<InstallmentsDTO> installments = new ArrayList<InstallmentsDTO>();
//		InstallmentsDTO installmentsObject = new InstallmentsDTO("day", 1, 2, "regular", "10", "EUR");
//		InstallmentsDTO installmentsObject1 = new InstallmentsDTO("hour", 1, 2, "regular", "10", "EUR");
//		installments.add(installmentsObject);
//		installments.add(installmentsObject1);
//
//		PlanDTO planDTO = new PlanDTO("LOCAL_TEST_PLAN", installments);
//		planDTO.setCode("" + random.nextInt(100000));
//		planDTO.setCarryForwardAmount(true);
//		planDTO.setPaymentFailureThreshold(3);
//
//		String finalData = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setPlanDetails(planDTO).createPlan();
//		System.out.println(finalData);
//	}
//
//	public static void createSubscription() throws ApplicationError {
//
//		AddressDTO addressDetails = Utils.getAddressDetails();
//		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
//		RecurringDTO recurringDTO = new RecurringDTO(RecurringSourceEnum.CIT, RecurringTypeEnum.RECURRING,
//				RecurringReasonEnum.REAUTHORIZATION);
//		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.TEN, "EUR");
//		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
//		transactionDetails.setRecurring(recurringDTO);
//
//		List<NotificationChannels> notificationChannels = new ArrayList<>();
//		NotificationChannels notificationChannels1 = new NotificationChannels(NotificationChannelEnum.WHATSAPP,
//				"+919354237412");
//		NotificationChannels notificationChannels2 = new NotificationChannels(NotificationChannelEnum.SMS,
//				"+919354237412");
//		NotificationChannels notificationChannels3 = new NotificationChannels(NotificationChannelEnum.EMAIL,
//				"priyanshu@celerispay.com");
//		notificationChannels.add(notificationChannels1);
//		notificationChannels.add(notificationChannels2);
//		notificationChannels.add(notificationChannels3);
//		String subscription = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setBillingDetails(addressDetails).setTransactionDetails(transactionDetails)
//				.setUrlDetails(urlDetails).setStartDate(LocalDateTime.now().toString()).setQuantity(1)
//				.setNotificationChannel(notificationChannels).setPlanId(PLAN_ID).setAutomaticDebit(true)
//				.setTotalCycles(2).setExpireIn(15).createSubscription();
//		System.out.println(subscription);
//	}
//
//	public static void schedulerUpdateSubscription() throws ApplicationError {
//		ScheduleUpdateDTO scheduleUpdateDTO = new ScheduleUpdateDTO(PLAN_ID, "2025-04-16", "16-04-2025", 1);
//		String response = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
//				.setStaging(STAGING).setScheduleUpdateDTO(scheduleUpdateDTO)
//				.scheduleUpdateSubscription(SUBSCRIPTION_ID);
//		System.out.println(response);
//	}
//
//	private static void printLine() {
//		System.out.println(
//				"=================================================================================================================================================================================");
//	}
//
//}
//
///*
// *
// * MerchantDTO merchantDetails = new MerchantDTO(<MERCHANT_ID>, <CUSTOMER_ID>);
// * Certificate certificate = new Certificate(<FILE_PATH>, <KEY_PATH>); UrlDTO
// * urlDetails = new UrlDTO(<SUCCESS_URL>, <FAIL_URL>,<CANCEL_URL>);
// * TransactionDTO transactionDetails = new
// * TransactionDTO(<TXN_REFERENCE>,<TXN_AMOUNT>, <CURRENCY_CODE>);
// * transactionDetails.setSubscriptionId(<SUBSCRIPTION_ID>);
// *
// * AddressDTO addressDetails = new AddressDTO(<EMAIL_ID>); CardDetailsDTO
// * cardDetails = new CardDetailsDTO(<CARD_NUMBER>, <EXP_MONTH>, <EXP_YEAR>,
// * <CVV>, <CARD_HOLDER_NAME>, CardTypeEnum.<CARD_TYPE>, true);
// *
// * ExemptionsDTO exemptions = new ExemptionsDTO(<LOW_VALUE>, <TRA>,
// * <TRUSTED_BENEFICIARY>, <SECURE_CORPORATE_PAYMENT>,
// * <DELEGATED_AUTHENTICATION>, <RECURRING_MITEXEMPTION_SAMEAMOUNT>,
// * <RECURRING_MITEXEMPTION_OTHER>, <VMID>);
// *
// * DynamicDescriptorDTO descriptor = new DynamicDescriptorDTO(<NAME>);
// * descriptor.setEmail(<EMAIL_ID>); descriptor.setMobile(<MOBILE_NO>);
// *
// * // Additional details for transaction (Optional) List<ItemDTO> itemDetails =
// * new ArrayList<>(); itemDetails.add(new ItemDTO(<ITEM_ID>, <ITEM_NAME>,
// * <ITEM_QUANTITY>, <ITEM_PRICE_PER_UNIT>)); SummaryDetailsDTO summaryDetailsDTO
// * = new SummaryDTO(<SHIPPING_CHARGES>, <SUB_TOTAL>, <TAX>); DiscountDTO
// * discountDTO = new DiscountDTO(<COUPON_CODE_DETAILS>, <DISCOUNT_VALUE>,
// * <COUPON_CODE>); SummaryDTO summaryDetails = new SummaryDTO(summaryDetailsDTO,
// * discountDTO, <TOTAL_VALUE>); CustomDataDTO customDataDetails = new
// * CustomDataDTO(<CUSTOM_DATA_1>, <CUSTOM_DATA_2>, <CUSTOM_DATA_3>,
// * <CUSTOM_DATA_4>, <CUSTOM_DATA_5>, <SITE>);
// *
// * Map<String, Object> finalResponse = PaymentFactory.getInstance(certificate,
// * merchantDetails, <ACCESS_TOKEN>) .setStaging(STAGING)
// * .setTransactionDetails(transactionDetails) .setBillingDetails(addressDetails)
// * .setUrlDetails(urlDetails) .setDeviceFingerprint(deviceFingerprint)
// * .setItemDetails(itemDetails) .setSummaryDetails(summaryDetails)
// * .setCustomDataDetails(customDataDetails) .setExemptions(exemptions)
// * .setCardDetails(cardDetails) .setDynamicDescriptor(descriptor)
// * .buildPayment();
// *
// */
