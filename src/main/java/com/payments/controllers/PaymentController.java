package com.payments.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.payments.services.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
}
