package com.payments.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.gateway.constants.enums.AdjustmentTypeEnum;
import com.gateway.constants.enums.PeriodEnum;
import com.gateway.encryption.Certificate;
import com.gateway.exception.ApplicationError;
import com.gateway.payment.MerchantDTO;
import com.gateway.subscription.ConditionDTO;
import com.gateway.subscription.InstallmentsDTO;
import com.gateway.subscription.PlanDTO;
import com.gateway.subscription.RetryDTO;
import com.gateway.subscription.SubscriptionFactory;
import com.payments.commons.TestConstants;

@Log4j2
@Service
public class PlanService {

    private static final String ACCESS_TOKEN = TestConstants.TEST_WITH_ASHISH_ACCESS_TOKEN;
    private static final MerchantDTO MERCHANT_DETAILS = new MerchantDTO("TES250325001", TestConstants.getUniqueRef("TEST-CUS"));
    private static final Certificate CERTIFICATE = new Certificate(TestConstants.TEST_WITH_ASHISH_CERT_PATH, null);

    private final Random random = new Random();

    public String createPlan() throws ApplicationError {
        List<InstallmentsDTO> installments = new ArrayList<>();
        installments.add(new InstallmentsDTO("day", 1, 2, "regular", "10", "EUR"));

        PlanDTO planDTO = new PlanDTO("BARAL_PLAN", installments);
        planDTO.setCode("" + random.nextInt(100000));
        planDTO.setCarryForwardAmount(true);
        planDTO.setPaymentFailureThreshold(3);
        log.info("Plan Details: {}", planDTO);

        return (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).setPlanDetails(planDTO).createPlan();
    }

    public Object createRuleWithMacCode() throws ApplicationError {
        List<InstallmentsDTO> installments = List.of(
                new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR"),
                new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR")
        );
        RetryDTO retryDTO = new RetryDTO(1, 1, PeriodEnum.DAYS);
        retryDTO.setConditions(List.of(new ConditionDTO("25")));
        retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);

        PlanDTO planDetails = new PlanDTO("Recovery Rules", UUID.randomUUID().toString(), installments);
        planDetails.setRetry(List.of(retryDTO));

        return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).setPlanDetails(planDetails).createPlan();
    }

    public Object createRuleWithoutMacCode() throws ApplicationError {
        List<InstallmentsDTO> installments = List.of(
                new InstallmentsDTO("MONTH", 1, 10, "regular", "10.00", "EUR"),
                new InstallmentsDTO("MONTH", 1, 5, "regular", "20.00", "EUR")
        );

        RetryDTO retry = new RetryDTO(1, 1, PeriodEnum.DAY_OF_WEEK, 5);
        retry.setTime("22:00");
        retry.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);

        RetryDTO retry1 = new RetryDTO(2, 1, PeriodEnum.DAY_OF_WEEK, 5);
        retry1.setTime("22:30");
        retry1.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);

        RetryDTO retry2 = new RetryDTO(3, 8, PeriodEnum.DAY_OF_WEEK, 5);
        retry2.setTime("23:00");
        retry2.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 1);

        PlanDTO planDetails = new PlanDTO("Test plan", UUID.randomUUID().toString(), installments);
        planDetails.setRetry(List.of(retry, retry1, retry2));

        return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).setPlanDetails(planDetails).createPlan();
    }

    public Object updateRecoveryRule(String planId) throws ApplicationError {
        List<InstallmentsDTO> installments = List.of(
                new InstallmentsDTO("MONTH", 1, 6, "regular", "10.00", "EUR"),
                new InstallmentsDTO("MONTH", 1, 3, "regular", "20.00", "EUR")
        );

        RetryDTO retryDTO = new RetryDTO(1, 2, PeriodEnum.DAYS);
        retryDTO.setConditions(List.of(new ConditionDTO("25")));
        retryDTO.setDiscount(AdjustmentTypeEnum.PERCENTAGE, 2);

        PlanDTO planDetails = new PlanDTO("Recovery Rules Updated", installments);
        planDetails.setRetry(List.of(retryDTO));

        return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).setPlanDetails(planDetails).updateSubscriptionPlan(planId);
    }

    public Object getRule(String planId) throws ApplicationError {
        return SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).getSubscriptionPlan(planId);
    }

    public String getRecoveryRule(String planId) throws ApplicationError {
        return (String) SubscriptionFactory.getInstance(CERTIFICATE, MERCHANT_DETAILS, ACCESS_TOKEN)
                .setStaging(false).getSubscriptionPlan(planId);
    }
}