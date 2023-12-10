package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class Integer16BitDecimalToken extends NumericToken {

	public Integer16BitDecimalToken(String sourceFragment) {
		super(sourceFragment);
	}

	public Integer16BitDecimalToken(int value) {
		this(String.valueOf(value));
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitInteger16BitDecimal(this);
	}

	public int getValue() {
		return getInt();
	}

}