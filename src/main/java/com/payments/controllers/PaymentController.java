package com.payments.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.payments.beans.PaymentRequest;
import com.payments.commons.TestConstants;
import com.payments.services.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;

@Log4j2
@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("home");
	}

	@PostMapping(value = {"/payment-api", "/paymentApi"})
	public String paymentApi(@RequestBody PaymentRequest paymentRequest) {
		log.info(
				"Processing {} transactions with {} acquirer and capture transaction is {}, reconciliation is {} and staging is {}",
				paymentRequest.getCount(), paymentRequest.getAcquirer(), paymentRequest.isCaptureTxn(),
				paymentRequest.isReconciliation(), paymentRequest.isStaging());
		paymentService.payment(paymentRequest);
		return "success";
	}

	@CrossOrigin("*")
	@PostMapping("/payment-form")
	public String payment(@RequestBody PaymentRequest paymentRequest) {
		log.info(
				"Processing {} transactions with {} acquirer and capture transaction is {}, reconciliation is {} and staging is {}",
				paymentRequest.getCount(), paymentRequest.getAcquirer(), paymentRequest.isCaptureTxn(),
				paymentRequest.isReconciliation(), paymentRequest.isStaging());
		log.info("Received Payment request: {}", paymentRequest);
		paymentService.payment(paymentRequest);
		return "success";
	}

	@CrossOrigin("*")
	@GetMapping("/form-data")
	public String formData() {
		List<String> acquirers = List.of("acquired", "truevo", "credorax", "payxpert", "ecommpay", "payreto",
				"braintree", "nmi");
		List<String> merchants = List.of(TestConstants.TEST_WITH_ASHISH_MERCHANT_ID,
				TestConstants.WOOD_MERCHANT_MERCHANT_ID, TestConstants.TEST_ALGO_MERCHANT_ID,
				TestConstants.TEST_ALGO_GRANDCHILD_1_MERCHANT_ID, TestConstants.TEST_ALGO_GRANDCHILD_3_MERCHANT_ID,
				TestConstants.QA_TEST_MERCHANT_MERCHANT_ID);
		List<String> environments = List.of("prod", "stag", "dev", "local", "sushil_sir");
		String cardHolderName = new Faker().name().fullName();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("acquirers", acquirers);
		jsonObject.put("merchants", merchants);
		jsonObject.put("environments", environments);
		jsonObject.put("cardHolderName", cardHolderName);
		log.info("Form data: {}", jsonObject);
		return jsonObject.toString();
	}

	private int count = 0;
	private String url;
	private String code = "";

	@GetMapping("/{code}")
	public ResponseEntity<String> redirectUrl(@PathVariable String code) {
		System.out.println(code);
		System.out.println(this.code);
		if (!this.code.equalsIgnoreCase(code)) {
			return ResponseEntity.notFound().build();
		}
		count++;
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(this.url)).build();
	}

	@GetMapping("/{code}/analytics")
	public ResponseEntity<String> analytics(@PathVariable String code) {
		if (!this.code.equalsIgnoreCase(code)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok("count: " + count);
	}

	@PostMapping("/url")
	public ResponseEntity<String> shortenUrl(@RequestParam String url) {
		this.code = UUID.randomUUID().toString().replaceAll(Pattern.quote("-"), "").trim().substring(0, 8);
		System.out.println(this.code);
		this.url = url;
		return new ResponseEntity<>("http://192.168.1.103:8000/" + this.code, HttpStatus.CREATED);
	}
}
