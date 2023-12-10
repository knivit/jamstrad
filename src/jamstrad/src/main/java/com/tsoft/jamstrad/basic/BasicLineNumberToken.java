package com.tsoft.jamstrad.basic;

public class BasicLineNumberToken extends BasicSourceToken {

	public BasicLineNumberToken(String sourceFragment) {
		super(sourceFragment);
	}

	public BasicLineNumberToken(int lineNumber) {
		this(String.valueOf(lineNumber));
	}

	public int getLineNumber() {
		return Integer.parseInt(getSourceFragment());
	}

}