package com.tsoft.jamstrad.basic.locomotive.minify;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicMinifier;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceCodeLine;

public abstract class LocomotiveBasicMinifier implements BasicMinifier {

	protected LocomotiveBasicMinifier() {
	}

	protected void updateLine(BasicSourceCode sourceCode, BasicSourceTokenSequence sequence) throws BasicException {
		if (sequence.isModified()) {
			sourceCode.addLine(new LocomotiveBasicSourceCodeLine(sequence.getSourceCode()));
		}
	}

}