package com.payments.commons;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;

import org.springframework.util.StringUtils;

import com.gateway.constants.enums.CardTypeEnum;
import com.gateway.constants.enums.RecurringReasonEnum;
import com.gateway.constants.enums.RecurringSourceEnum;
import com.gateway.constants.enums.RecurringTypeEnum;
import com.gateway.dto.RecurringDTO;
import com.gateway.payment.AddressDTO;
import com.gateway.payment.BillingDTO;
import com.gateway.payment.CardDetailsDTO;
import com.gateway.payment.CustomerDTO;
import com.gateway.payment.DynamicDescriptorDTO;
import com.gateway.payment.ShippingDTO;
import com.gateway.payment.UrlDTO;
import com.payments.beans.PaymentRequest;

import net.datafaker.Faker;

public class Utils {

	private static final Faker faker = new Faker();

	public static CustomerDTO getCustomerDetails() {
		AddressDTO addressDTO = getAddressDetails();
		CustomerDTO customerDTO = new CustomerDTO(new BillingDTO(addressDTO), new ShippingDTO(addressDTO));
		Random random = new Random();
		boolean isIp4V = random.nextBoolean();
		if (isIp4V) {
			customerDTO.setIpAddress(faker.internet().ipV4Address());
		} else {
			customerDTO.setIpAddress(faker.internet().ipV6Address());
		}
		customerDTO.setBillingAddress(new BillingDTO(getAddressDetails()));
		customerDTO.setShippingAddress(new ShippingDTO(getAddressDetails()));
		customerDTO.setDob(faker.date().birthday().toString());
		return customerDTO;
	}

	public static AddressDTO getAddressDetails() {
		AddressDTO addressDetails = new AddressDTO(faker.internet().emailAddress());
		addressDetails.setCountry(faker.address().countryCode());
		addressDetails.setCity(faker.address().city());
		addressDetails.setZip(faker.address().zipCode());
		addressDetails.setAddressLine1(faker.address().streetAddress());
		addressDetails.setAddressLine2(faker.address().secondaryAddress());
		addressDetails.setMobileNo(faker.phoneNumber().cellPhone());
		addressDetails.setFirstName(faker.name().firstName());
		addressDetails.setLastName(faker.name().lastName());
		addressDetails.setState(faker.address().state());
		return addressDetails;
	}

	public static CardDetailsDTO getCardDetails(PaymentRequest paymentRequest) {
		Random random = new Random();
		CardDetailsDTO cardDetails = new CardDetailsDTO();
		if (!StringUtils.hasLength(paymentRequest.getCardNumber())) {
			cardDetails
					.setCardNumber(TestConstants.SUCCESS_CARD.get(random.nextInt(TestConstants.SUCCESS_CARD.size())));
		} else {
			cardDetails.setCardNumber(paymentRequest.getCardNumber());
		}
		cardDetails.setExpMonth("04");
		cardDetails.setExpYear("2028");
		cardDetails.setCvv("000");
		cardDetails.setNameOnCard(faker.name().fullName());
		cardDetails.setCardType(CardTypeEnum.MASTER_CARD);
		cardDetails.setCardType(TestConstants.MASTERCARD.equalsIgnoreCase(cardDetails.getCardNumber())
				? CardTypeEnum.MASTER_CARD
				: CardTypeEnum.VISA_CARD);
		return cardDetails;
	}

	public static UrlDTO getUrlDetails() {
		return new UrlDTO(TestConstants.SUCCESS, TestConstants.FAIL, TestConstants.CANCEL);
	}

	public static DynamicDescriptorDTO getDynamicDescriptor() {
		DynamicDescriptorDTO descriptor = new DynamicDescriptorDTO(faker.name().fullName());
		descriptor.setEmail(faker.internet().emailAddress());
		descriptor.setMobile(faker.phoneNumber().phoneNumber());
		return descriptor;
	}

	public static RecurringDTO getRecurringDetails() {
		return new RecurringDTO(RecurringSourceEnum.MIT, RecurringTypeEnum.RECURRING,
				RecurringReasonEnum.REAUTHORIZATION);
	}

	public static String getAccessToken(Boolean staging) {
		return staging
				? TestConstants.LOCAL_TEST_WITH_ASHISH_ACCESS_TOKEN
				: TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN;
	}

	public static String getEpochTime(LocalDateTime date) {
		ZoneId zoneId = ZoneOffset.systemDefault();
		Instant instant = date.atZone(zoneId).toInstant();
		ZoneOffset offset = zoneId.getRules().getOffset(instant);
		return String.valueOf(date.toEpochSecond(offset));
	}

}
