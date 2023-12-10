package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class FloatingPointTypedVariableToken extends TypedVariableToken {

	public static final char TYPE_INDICATOR = '!';

	public static FloatingPointTypedVariableToken forName(String variableNameWithoutTypeIndicator) {
		return new FloatingPointTypedVariableToken(variableNameWithoutTypeIndicator + TYPE_INDICATOR);
	}

	public FloatingPointTypedVariableToken(String sourceFragment) {
		super(sourceFragment);
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitFloatingPointTypedVariable(this);
	}

	@Override
	public char getTypeIndicator() {
		return TYPE_INDICATOR;
	}

}