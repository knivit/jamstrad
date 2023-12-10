package com.tsoft.jamstrad.basic.locomotive;

import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSyntaxException;

public class LocomotiveBasicSourceCodeLine extends BasicSourceCodeLine {

	public LocomotiveBasicSourceCodeLine(String text) throws BasicSyntaxException {
		super(BasicLanguage.LOCOMOTIVE_BASIC, text);
	}

	@Override
	public LocomotiveBasicSourceCodeLine clone() {
		return (LocomotiveBasicSourceCodeLine) super.clone();
	}

	@Override
	public LocomotiveBasicSourceCodeLineScanner createScanner() {
		return new LocomotiveBasicSourceCodeLineScanner(getText());
	}

}