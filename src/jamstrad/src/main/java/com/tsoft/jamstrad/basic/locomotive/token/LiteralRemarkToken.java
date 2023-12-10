package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class LiteralRemarkToken extends AbstractLiteralToken {

	public LiteralRemarkToken(String sourceFragment) {
		super(sourceFragment);
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitLiteralRemark(this);
	}

}