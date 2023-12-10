package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class StringTypedVariableToken extends TypedVariableToken {

	public static final char TYPE_INDICATOR = '$';

	public static StringTypedVariableToken forName(String variableNameWithoutTypeIndicator) {
		return new StringTypedVariableToken(variableNameWithoutTypeIndicator + TYPE_INDICATOR);
	}

	public StringTypedVariableToken(String sourceFragment) {
		super(sourceFragment);
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitStringTypedVariable(this);
	}

	@Override
	public char getTypeIndicator() {
		return TYPE_INDICATOR;
	}

}