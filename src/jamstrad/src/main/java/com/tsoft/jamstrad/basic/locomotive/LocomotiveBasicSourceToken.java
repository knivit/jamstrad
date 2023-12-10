package com.tsoft.jamstrad.basic.locomotive;

import com.tsoft.jamstrad.basic.BasicSourceToken;

public abstract class LocomotiveBasicSourceToken extends BasicSourceToken {

	protected LocomotiveBasicSourceToken(String sourceFragment) {
		super(sourceFragment);
	}

	public abstract void invite(LocomotiveBasicSourceTokenVisitor visitor);

}