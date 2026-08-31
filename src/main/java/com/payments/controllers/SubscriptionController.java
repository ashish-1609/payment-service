package com.payments.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.exception.ApplicationError;
import com.payments.services.PlanService;
import com.payments.services.SubscriptionPaymentService;
import com.payments.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscription")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscriptionController {

	private final PlanService planService;
	private final SubscriptionService subscriptionService;
	private final SubscriptionPaymentService paymentService;

	// ==========================================
	// PAYMENT & TRANSACTION ENDPOINTS
	// ==========================================

	@PostMapping("/payments/standard")
	public ResponseEntity<?> standardTransaction(@RequestParam String subscriptionId) throws ApplicationError {
		Map<String, Object> response = paymentService.createStandardTransaction(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/payments/initial")
	public ResponseEntity<?> initialPayment(@RequestParam String subscriptionId) throws ApplicationError {
		Map<String, Object> response = paymentService.initialPaymentCreatePayment(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/payments/express")
	public ResponseEntity<?> expressPayment(@RequestParam String subscriptionId) throws ApplicationError {
		Map<String, Object> response = paymentService.initialSubscriptionCreateExpressPaymentWithCard(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/payments/rebill/no-cvv")
	public ResponseEntity<?> rebillNoCvv(@RequestParam String subscriptionId) throws ApplicationError {
		Map<String, Object> response = paymentService.subscriptionSubsequentRebillingWithoutCVV(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/payments/rebill/cvv")
	public ResponseEntity<?> rebillWithCvv(@RequestParam String subscriptionId) throws ApplicationError {
		Map<String, Object> response = paymentService.subscriptionSubsequentRebillingWithCVV(subscriptionId);
		return ResponseEntity.ok(response);
	}

	// ==========================================
	// PLAN MANAGEMENT ENDPOINTS
	// ==========================================

	@PostMapping("/plans")
	public ResponseEntity<?> createPlan() throws ApplicationError {
		String response = planService.createPlan();
		return ResponseEntity.ok(response);
	}

	@PostMapping("/plans/rule/mac")
	public ResponseEntity<?> createRuleWithMac() throws ApplicationError {
		Object response = planService.createRuleWithMacCode();
		return ResponseEntity.ok(response);
	}

	@PostMapping("/plans/rule/no-mac")
	public ResponseEntity<?> createRuleWithoutMac() throws ApplicationError {
		Object response = planService.createRuleWithoutMacCode();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/plans/{planId}/recovery-rule")
	public ResponseEntity<?> updateRecoveryRule(@PathVariable String planId) throws ApplicationError {
		Object response = planService.updateRecoveryRule(planId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/plans/{planId}/rule")
	public ResponseEntity<?> getRule(@PathVariable String planId) throws ApplicationError {
		Object response = planService.getRule(planId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/plans/{planId}/recovery-rule")
	public ResponseEntity<?> getRecoveryRule(@PathVariable String planId) throws ApplicationError {
		String response = planService.getRecoveryRule(planId);
		return ResponseEntity.ok(response);
	}

	// ==========================================
	// SUBSCRIPTION MANAGEMENT ENDPOINTS
	// ==========================================

	@PostMapping("/create")
	public ResponseEntity<?> createSubscription(@RequestParam String planId) throws ApplicationError {
		String response = subscriptionService.createSubscription(planId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{subscriptionId}")
	public ResponseEntity<?> getSubscriptionDetails(@PathVariable String subscriptionId) throws ApplicationError {
		Object response = subscriptionService.getSubscriptionDetails(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{subscriptionId}/deactivate")
	public ResponseEntity<?> deactivateSubscription(@PathVariable String subscriptionId) throws ApplicationError {
		Object response = subscriptionService.deactivateSubscription(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{subscriptionId}/reactivate")
	public ResponseEntity<?> reactivateSubscription(@PathVariable String subscriptionId) throws ApplicationError {
		String response = subscriptionService.reactivateSubscription(subscriptionId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{subscriptionId}/schedule")
	public ResponseEntity<?> scheduleUpdate(@RequestParam String planId, @PathVariable String subscriptionId)
			throws ApplicationError {
		String response = subscriptionService.schedulerUpdateSubscription(planId, subscriptionId);
		return ResponseEntity.ok(response);
	}
}