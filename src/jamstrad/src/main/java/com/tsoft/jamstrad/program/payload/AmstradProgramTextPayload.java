package com.tsoft.jamstrad.program.payload;

import com.tsoft.jamstrad.program.AmstradProgramException;

public abstract class AmstradProgramTextPayload extends AmstradProgramPayload {

	protected AmstradProgramTextPayload() {
	}

	@Override
	public final boolean isText() {
		return true;
	}

	@Override
	protected final AmstradProgramBinaryPayload toBinaryPayload() {
		return null;
	}

	@Override
	protected final AmstradProgramTextPayload toTextPayload() {
		return this;
	}

	public abstract CharSequence getText() throws AmstradProgramException;

}