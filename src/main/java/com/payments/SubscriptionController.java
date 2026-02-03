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
import com.gateway.constants.enums.PaymentModeEnum;
import com.gateway.constants.enums.PeriodEnum;
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
import com.gateway.subscription.ConditionDTO;
import com.gateway.subscription.InstallmentsDTO;
import com.gateway.subscription.PlanDTO;
import com.gateway.subscription.RetryDTO;
import com.gateway.subscription.ScheduleUpdateDTO;
import com.gateway.subscription.SubscriptionFactory;
import com.payments.commons.TestConstants;
import com.payments.services.FormBuilderService;

public class SubscriptionController {

	public static final String SUCCESS = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String FAIL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String CANCEL = "http://192.168.1.17:8084/Views/Home/Response.aspx?uniqueId=f8523633-befc-440a-9c25-95489c3520ae";
	public static final String ACCESS_TOKEN = TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN;
	public static final Random random = new Random();
	public static final MerchantDTO MERCHANT_DETAILS = new MerchantDTO("TES250325001",
			TestConstants.getUniqueRef("TEST-CUS"));
	public static final Certificate CERTIFICATE = new Certificate("D:\\Certificates\\dev\\TES250325001-crt.pem", null);
	public static final String PLAN_ID = "c45fbffe-bcb7-43ee-91d5-af519609229a";
	private static final String SUBSCRIPTION_ID = "f5f58393-f600-4d1e-a6b6-b2ed463d0f0d";

	public static void main(String[] args) throws ApplicationError {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			printLine();
			System.out.println("Subscription");
			printLine();
			System.out.println("What do you want to do?");
			System.out.println("\n    1.     Create Plan\n    " + "2.     Create Subscription \n    "
					+ "3.     Deactivate Subscription " + "\n    " + "4.     Reactivate Subscription" + "\n    "
					+ "5.     Get Subscription Details \n    " + "6.     Create Rule With Mac Code \n    "
					+ "7.     Create Rule Without Mac Code \n    " + "8.     get Rule \n    "
					+ "9.     Initial Subscription-Create Payment \n    " + "10.    Update Recovery Rule \n    "
					+ "11.    Get Recovery Rule \n    "
					+ "12.    Subscription Subsequent Re-billing Without CVV   \n    "
					+ "13.    Subscription Subsequent Re-billing With CVV    \n    "
					+ "14.    Initial Subscription create Express Payment With Card \n    "
					+ "15.    Scheduler Update Subscription \n    " + "\n");
			printLine();
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			printLine();
			switch (choice) {
				case 0 :
					transaction();
					break;
				case 1 :
					createPlan();
					break;
				case 2 :
					createSubscription();
					break;
				case 3 :
					deactivateSubscription();
					break;
				case 4 :
					reactivateSubscription();
					break;
				case 5 :
					getSubscriptionDetails();
					break;
				case 6 :
					createRuleWithMacCode();
					break;
				case 7 :
					createRuleWithoutMacCode();
					break;
				case 8 :
					getRule();
					break;
				case 9 :
					initialPaymentCreatePayment();
					break;
				case 10 :
					updateRecoveryRule();
					break;
				case 11 :
					getRecoveryRule();
					break;
				case 12 :
					subscriptionSubsequentRebillingWithoutCVV();
					break;
				case 13 :
					subscriptionSubsequentRebillingWithCVV();
					break;
				case 14 :
					initialSubscriptionCreateExpressPaymentWithCard();
				case 15 :
					schedulerUpdateSubscription();
					break;
				default :
					break;
			}
		}

	}

	private static void transaction() throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		CardDetailsDTO cardDetails = new CardDetailsDTO();
		cardDetails.setCardNumber("4200000000000000");
		cardDetails.setExpMonth("04");
		cardDetails.setExpYear("2028");
		cardDetails.setCvv("123");
		cardDetails.setSaveDetails(true);
		cardDetails.setNameOnCard("John Smith");

		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(),
				BigDecimal.valueOf(new Random().nextInt(30) + .99), "EUR");
		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);

		Map<String, Object> stringObjectMap = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setTransactionDetails(transactionDetails).setUrlDetails(urlDetails)
				.setCardDetails(cardDetails).buildPayment();
		System.out.println(stringObjectMap);
		FormBuilderService formBuilder = new FormBuilderService();
		formBuilder.createForm(stringObjectMap);
	}

	private static void initialSubscriptionCreateExpressPaymentWithCard() throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.TEN, "EUR");
		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
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

		// Additional details for transaction (Optional)
		List<ItemDTO> itemDetails = new ArrayList<>();
		itemDetails.add(new ItemDTO(String.valueOf(new Random().nextInt(11111111)), "Item 1", 1, BigDecimal.ONE));
		SummaryDetailsDTO summaryDetailsDTO = new SummaryDetailsDTO("1", "1", "1");
		DiscountDTO discountDTO = new DiscountDTO("", "", "");
		SummaryDTO summaryDetails = new SummaryDTO(summaryDetailsDTO, discountDTO, "1");
		CustomDataDTO customDataDetails = new CustomDataDTO("", "", "", "", "", "");

		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT.getValue(),
				RecurringTypeEnum.RECURRING.getValue(), RecurringReasonEnum.REAUTHORIZATION.getValue());
		transactionDetails.setRecurring(recurring);

		Map<String, Object> finalResponse = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setTransactionDetails(transactionDetails).setBillingDetails(addressDetails)
				.setUrlDetails(urlDetails).setDynamicDescriptor(descriptor).setItemDetails(itemDetails)
				.setSummaryDetails(summaryDetails).setCustomDataDetails(customDataDetails)
				// .setCardDetails(cardDetails)
				.setPaymentMode(PaymentModeEnum.CREDIT_CARD)
				// .setDeviceFingerprint(deviceFingerprint)
				// .setExemptions(exemptions)
				// .setSDKDetails(threeDSdk)
				// .setChallengeConfig(ChallengeIndicatorEnum.<CHALLENGE_INDICATOR>,
				// ChallengeWindowEnum.<CHALLENGE_WINDOW>)
				.buildPayment();
		System.out.println(finalResponse);
		FormBuilderService formBuilder = new FormBuilderService();
		formBuilder.createForm(finalResponse);

	}

	private static void subscriptionSubsequentRebillingWithoutCVV() throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1.9),
				"EUR");
		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);

		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT.getValue(),
				RecurringTypeEnum.UNSCHEDULED.getValue(), RecurringReasonEnum.NOT_SHOW.getValue());
		transactionDetails.setRecurring(recurring);

		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000");

		Map<String, Object> response = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setUrlDetails(urlDetails).setBillingDetails(addressDetails)
				.setTransactionDetails(transactionDetails).setTokenDetails(tokenDetails).buildPayment();
		System.out.println(response);
		FormBuilderService formBuilder = new FormBuilderService();
		formBuilder.createForm(response);
	}

	private static void subscriptionSubsequentRebillingWithCVV() throws ApplicationError {
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		AddressDTO addressDetails = new AddressDTO("john@gmail.com");

		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(1),
				"EUR", PaymentModeEnum.CREDIT_CARD);
		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);

		RecurringDTO recurring = new RecurringDTO(RecurringSourceEnum.MIT.getValue(),
				RecurringTypeEnum.UNSCHEDULED.getValue(), RecurringReasonEnum.NOT_SHOW.getValue());
		transactionDetails.setRecurring(recurring);

		TokenDTO tokenDetails = new TokenDTO("420000-357625525218741531-0000", "123", true);

		Map<String, Object> response = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setUrlDetails(urlDetails).setBillingDetails(addressDetails)
				.setTransactionDetails(transactionDetails).setTokenDetails(tokenDetails).buildPayment();
		System.out.println(response);
		FormBuilderService formBuilder = new FormBuilderService();
		formBuilder.createForm(response);
	}

	private static void getRecoveryRule() throws ApplicationError {
		String subscriptionPlan = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).getSubscriptionPlan(PLAN_ID);
		System.out.println(subscriptionPlan);
	}

	private static void initialPaymentCreatePayment() throws ApplicationError {
		AddressDTO billingDetails = new AddressDTO("john@gmail.com");
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.ONE, "EUR");
		transactionDetails.setSubscriptionId(SUBSCRIPTION_ID);
		Map<String, Object> payment = PaymentFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setBillingDetails(billingDetails).setUrlDetails(urlDetails)
				.setTransactionDetails(transactionDetails).buildPayment();
		FormBuilderService formBuilder = new FormBuilderService();
		formBuilder.createForm(payment);
		System.out.println(payment);
	}

	public static void createRuleWithMacCode() throws ApplicationError {
		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR");
		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR");
		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
		RetryDTO retryDTO = new RetryDTO(1, 1, PeriodEnum.DAYS);

		retryDTO.setConditions(List.of(new ConditionDTO("25")));

		retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
		PlanDTO planDetails = new PlanDTO("Recovery Rules", UUID.randomUUID().toString(), installments);
		planDetails.setRetry(List.of(retryDTO));
		System.out.println(planDetails);
		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setPlanDetails(planDetails).createPlan();
		System.out.println(plan);
	}

	public static void createRuleWithoutMacCode() throws ApplicationError {
		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR");
		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR");
		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
		RetryDTO retry = new RetryDTO(1, 1, PeriodEnum.DAY_OF_WEEK, 5);
		retry.setTime("22:00");
		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
		RetryDTO retry1 = new RetryDTO(2, 1, PeriodEnum.DAY_OF_WEEK, 5);
		retry.setTime("22:30");
		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
		RetryDTO retry2 = new RetryDTO(3, 8, PeriodEnum.DAY_OF_WEEK, 5);
		retry.setTime("23:00");
		retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);
		PlanDTO planDetails = new PlanDTO("Test plan", UUID.randomUUID().toString(), installments);
		planDetails.setRetry(List.of(retry, retry1, retry2));
		System.out.println(planDetails);
		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setPlanDetails(planDetails).createPlan();
		System.out.println(plan);
	}

	private static void updateRecoveryRule() throws ApplicationError {
		InstallmentsDTO installmentsDTO1 = new InstallmentsDTO("MONTH", 1, 6, "regular", "10.00", "EUR");
		InstallmentsDTO installmentsDTO2 = new InstallmentsDTO("MONTH", 1, 3, "regular", "20.00", "EUR");
		List<InstallmentsDTO> installments = List.of(installmentsDTO1, installmentsDTO2);
		RetryDTO retryDTO = new RetryDTO(1, 2, PeriodEnum.DAYS);

		retryDTO.setConditions(List.of(new ConditionDTO("25")));

		retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 2);
		PlanDTO planDetails = new PlanDTO("Recovery Rules Updated", installments);
		planDetails.setRetry(List.of(retryDTO));
		System.out.println(planDetails);
		Object plan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN).setStaging(true)
				.setPlanDetails(planDetails).updateSubscriptionPlan(PLAN_ID);
		System.out.println(plan);
	}

	private static void getRule() throws ApplicationError {
		Object subscriptionPlan = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.getSubscriptionPlan("6aae4ead-74e1-4c50-8ebb-b3a1e8785bdc");
		System.out.println(subscriptionPlan);
	}

	private static void getSubscriptionDetails() throws ApplicationError {
		Object subscriptionDetails = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.getSubscriptionDetails(SUBSCRIPTION_ID);
		System.out.println(subscriptionDetails);
	}

	public static void deactivateSubscription() throws ApplicationError {
		Object object = SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setCustomRedirection(false).deactivateSubscription(SUBSCRIPTION_ID);
		System.out.println(object);
	}

	public static void reactivateSubscription() throws ApplicationError {
		String object = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).reactivateSubscription(SUBSCRIPTION_ID);

		System.out.println(object);
	}

	public static void createPlan() throws ApplicationError {
		List<InstallmentsDTO> installments = new ArrayList<InstallmentsDTO>();
		InstallmentsDTO installmentsObject = new InstallmentsDTO("month", 1, 12, "regular", "10", "EUR");
		installments.add(installmentsObject);

		PlanDTO planDTO = new PlanDTO("LOCAL_TEST_PLAN", installments);
		planDTO.setCode("" + random.nextInt(100000));
		planDTO.setCarryForwardAmount(true);
		planDTO.setPaymentFailureThreshold(3);

		String finalData = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setPlanDetails(planDTO).createPlan();
		System.out.println(finalData);
	}

	public static void createSubscription() throws ApplicationError {

		AddressDTO addressDetails = new AddressDTO("john@gmail.com");
		addressDetails.setFirstName("");
		addressDetails.setLastName("");
		UrlDTO urlDetails = new UrlDTO(SUCCESS, FAIL, CANCEL);
		RecurringDTO recurringDTO = new RecurringDTO(RecurringSourceEnum.CIT.getValue(),
				RecurringTypeEnum.RECURRING.getValue(), RecurringReasonEnum.REAUTHORIZATION.getValue());
		TransactionDTO transactionDetails = new TransactionDTO(UUID.randomUUID().toString(), true);
		transactionDetails.setRecurring(recurringDTO);

		String subscription = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setBillingDetails(addressDetails).setTransactionDetails(transactionDetails)
				.setUrlDetails(urlDetails).setStartDate(LocalDateTime.now().toString()).setQuantity(1)
				.setPlanId(PLAN_ID).setAutomaticDebit(true).setTotalCycles(2).setExpireIn(15).createSubscription();
		System.out.println(subscription);
	}

	public static void schedulerUpdateSubscription() throws ApplicationError {
		ScheduleUpdateDTO scheduleUpdateDTO = new ScheduleUpdateDTO(PLAN_ID, "2025-04-16", "16-04-2025", 1);
		String response = (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
				.setStaging(true).setScheduleUpdateDTO(scheduleUpdateDTO).scheduleUpdateSubscription(SUBSCRIPTION_ID);
		System.out.println(response);
	}

	private static void printLine() {
		System.out.println(
				"=================================================================================================================================================================================");
	}

}

/*
 *
 * MerchantDTO merchantDetails = new MerchantDTO(<MERCHANT_ID>, <CUSTOMER_ID>);
 * Certificate certificate = new Certificate(<FILE_PATH>, <KEY_PATH>); UrlDTO
 * urlDetails = new UrlDTO(<SUCCESS_URL>, <FAIL_URL>,<CANCEL_URL>);
 * TransactionDTO transactionDetails = new
 * TransactionDTO(<TXN_REFERENCE>,<TXN_AMOUNT>, <CURRENCY_CODE>);
 * transactionDetails.setSubscriptionId(<SUBSCRIPTION_ID>);
 *
 * AddressDTO addressDetails = new AddressDTO(<EMAIL_ID>); CardDetailsDTO
 * cardDetails = new CardDetailsDTO(<CARD_NUMBER>, <EXP_MONTH>, <EXP_YEAR>,
 * <CVV>, <CARD_HOLDER_NAME>, CardTypeEnum.<CARD_TYPE>, true);
 *
 * ExemptionsDTO exemptions = new ExemptionsDTO(<LOW_VALUE>, <TRA>,
 * <TRUSTED_BENEFICIARY>, <SECURE_CORPORATE_PAYMENT>,
 * <DELEGATED_AUTHENTICATION>, <RECURRING_MITEXEMPTION_SAMEAMOUNT>,
 * <RECURRING_MITEXEMPTION_OTHER>, <VMID>);
 *
 * DynamicDescriptorDTO descriptor = new DynamicDescriptorDTO(<NAME>);
 * descriptor.setEmail(<EMAIL_ID>); descriptor.setMobile(<MOBILE_NO>);
 *
 * // Additional details for transaction (Optional) List<ItemDTO> itemDetails =
 * new ArrayList<>(); itemDetails.add(new ItemDTO(<ITEM_ID>, <ITEM_NAME>,
 * <ITEM_QUANTITY>, <ITEM_PRICE_PER_UNIT>)); SummaryDetailsDTO summaryDetailsDTO
 * = new SummaryDTO(<SHIPPING_CHARGES>, <SUB_TOTAL>, <TAX>); DiscountDTO
 * discountDTO = new DiscountDTO(<COUPON_CODE_DETAILS>, <DISCOUNT_VALUE>,
 * <COUPON_CODE>); SummaryDTO summaryDetails = new SummaryDTO(summaryDetailsDTO,
 * discountDTO, <TOTAL_VALUE>); CustomDataDTO customDataDetails = new
 * CustomDataDTO(<CUSTOM_DATA_1>, <CUSTOM_DATA_2>, <CUSTOM_DATA_3>,
 * <CUSTOM_DATA_4>, <CUSTOM_DATA_5>, <SITE>);
 *
 * Map<String, Object> finalResponse = PaymentFactory.getInstance(certificate,
 * merchantDetails, <ACCESS_TOKEN>) .setStaging(true)
 * .setTransactionDetails(transactionDetails) .setBillingDetails(addressDetails)
 * .setUrlDetails(urlDetails) .setDeviceFingerprint(deviceFingerprint)
 * .setItemDetails(itemDetails) .setSummaryDetails(summaryDetails)
 * .setCustomDataDetails(customDataDetails) .setExemptions(exemptions)
 * .setCardDetails(cardDetails) .setDynamicDescriptor(descriptor)
 * .buildPayment();
 *
 */
