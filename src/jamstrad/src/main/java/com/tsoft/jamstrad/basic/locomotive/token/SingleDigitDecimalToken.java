package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class SingleDigitDecimalToken extends NumericToken {

	public SingleDigitDecimalToken(String sourceFragment) {
		super(sourceFragment);
	}

	public SingleDigitDecimalToken(int value) {
		this(String.valueOf(value));
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitSingleDigitDecimal(this);
	}

	public int getValue() {
		return getInt();
	}

}