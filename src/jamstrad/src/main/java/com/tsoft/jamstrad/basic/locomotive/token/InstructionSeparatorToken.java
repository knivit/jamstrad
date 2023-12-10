package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceToken;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class InstructionSeparatorToken extends LocomotiveBasicSourceToken {

	public static final char SEPARATOR = ':';

	public InstructionSeparatorToken() {
		super(String.valueOf(SEPARATOR));
	}
	
	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitInstructionSeparator(this);
	}

}