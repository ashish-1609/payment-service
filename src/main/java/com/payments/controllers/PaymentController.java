package com.payments.controllers;

import net.datafaker.Faker;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.payments.PaymentRequest;
import com.payments.services.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("home");
	}

	@PostMapping("/payment")
	public String payment(@RequestParam(name = "count", required = false) int count,
			@RequestParam(name = "acquirer", required = false) String acquirer,
			@RequestParam(name = "capture", required = false) boolean capture,
			@RequestParam(name = "reconciliation", required = false) boolean reconciliation,
			@RequestParam(name = "subscription", required = false) boolean subscription,
			@RequestParam(name = "staging", required = false) boolean staging) {
		log.info(
				"Processing {} transactions with {} acquirer and capture transaction is {}, reconciliation is {} and staging is {}",
				count, acquirer, capture, reconciliation, staging);
		paymentService.payment(count, acquirer, capture, reconciliation, subscription, staging);
		return "success";
	}

	@CrossOrigin("*")
	@PostMapping("/payment-form")
	public String payment(@RequestBody PaymentRequest paymentRequest) {
		log.info(
				"Processing {} transactions with {} acquirer and capture transaction is {}, reconciliation is {} and staging is {}",
				paymentRequest.getNumberOfTransactions(), paymentRequest.getAcquirer(), paymentRequest.isCaptureTxn(),
				paymentRequest.isReconciliation(), paymentRequest.isStaging());
		paymentService.payment(paymentRequest.getNumberOfTransactions(), paymentRequest.getAcquirer(),
				paymentRequest.isCaptureTxn(), paymentRequest.isReconciliation(), paymentRequest.isSubscription(),
				paymentRequest.isStaging());
		return "success";
	}

	@CrossOrigin("*")
	@GetMapping("/form-data")
	public String formData() {
		List<String> acquirers = List.of("acquired", "truevo", "credorax", "payxpert", "ecommpay");
		List<String> merchants = List.of("TestWithAshish");
		List<String> environments = List.of("production", "staging", "development", "local");
		String cardHolderName = new Faker().name().fullName();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("acquirers", acquirers);
		jsonObject.put("merchants", merchants);
		jsonObject.put("environments", environments);
		jsonObject.put("cardHolderName", cardHolderName);
		log.info("Form data: {}", jsonObject);
		return jsonObject.toString();
	}
}
