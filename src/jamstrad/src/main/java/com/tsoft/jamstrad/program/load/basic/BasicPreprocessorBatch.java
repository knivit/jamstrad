package com.tsoft.jamstrad.program.load.basic;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;

public class BasicPreprocessorBatch implements Iterable<BasicPreprocessor> {

	private List<BasicPreprocessor> preprocessors;

	public BasicPreprocessorBatch() {
		this.preprocessors = new Vector<BasicPreprocessor>();
	}

	public void preprocess(BasicSourceCode sourceCode, AmstradProgramLoaderSession session) throws BasicException {
		for (BasicPreprocessor preprocessor : getPreprocessors()) {
			preprocessor.preprocess(sourceCode, session);
		}
	}

	public void add(BasicPreprocessor preprocessor) {
		getPreprocessors().add(preprocessor);
	}

	public void remove(BasicPreprocessor preprocessor) {
		getPreprocessors().remove(preprocessor);
	}

	@Override
	public Iterator<BasicPreprocessor> iterator() {
		return getPreprocessors().iterator();
	}

	private List<BasicPreprocessor> getPreprocessors() {
		return preprocessors;
	}

}