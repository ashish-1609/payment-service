package com.payments.enums;

import lombok.Getter;

public enum Environment {

	LOCAL("local"), DEV("dev"), STAG("stag"), PROD("stag"), SUSHIL_SIR("sushil_sir");

	@Getter
	final String value;

	private Environment(String value) {
		this.value = value;
	}
}
