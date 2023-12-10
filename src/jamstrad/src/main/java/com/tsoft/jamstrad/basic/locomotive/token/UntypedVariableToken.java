package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class UntypedVariableToken extends VariableToken {

	public static UntypedVariableToken forName(String variableNameWithoutTypeIndicator) {
		return new UntypedVariableToken(variableNameWithoutTypeIndicator);
	}

	public UntypedVariableToken(String sourceFragment) {
		super(sourceFragment);
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitUntypedVariable(this);
	}

	@Override
	public String getVariableNameWithoutTypeIndicator() {
		return getSourceFragment();
	}

	@Override
	protected String getCanonicalSourceForm() {
		return getVariableNameWithoutTypeIndicator().toUpperCase() + FloatingPointTypedVariableToken.TYPE_INDICATOR;
	}

}