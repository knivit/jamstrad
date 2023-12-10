package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class Integer16BitBinaryToken extends NumericToken {

	public Integer16BitBinaryToken(String sourceFragment) {
		super(sourceFragment);
	}

	@Override
	protected int parseAsInt() {
		return Integer.parseInt(getSourceFragment().substring(2), 2); // ex. &X11010
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitInteger16BitBinary(this);
	}

	public int getValue() {
		return getInt();
	}

}
