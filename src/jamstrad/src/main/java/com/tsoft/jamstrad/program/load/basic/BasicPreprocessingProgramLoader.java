package com.tsoft.jamstrad.program.load.basic;

import java.util.Iterator;

import com.tsoft.jamstrad.basic.BasicCode;
import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;

public class BasicPreprocessingProgramLoader extends BasicProgramLoader {

	private BasicPreprocessorBatch preprocessorBatch;

	public BasicPreprocessingProgramLoader(AmstradPc amstradPc) {
		super(amstradPc);
		this.preprocessorBatch = new BasicPreprocessorBatch();
	}

	public void addPreprocessor(BasicPreprocessor preprocessor) {
		getPreprocessorBatch().add(preprocessor);
	}

	public void removePreprocessor(BasicPreprocessor preprocessor) {
		getPreprocessorBatch().remove(preprocessor);
	}

	public Iterator<BasicPreprocessor> getPreprocessors() {
		return getPreprocessorBatch().iterator();
	}

	@Override
	protected BasicCode retrieveCode(AmstradProgram program, AmstradProgramLoaderSession session)
			throws AmstradProgramException {
		BasicSourceCode sourceCode = retrieveSourceCode(program);
		preprocess(sourceCode, session);
		return sourceCode;
	}

	protected void preprocess(BasicSourceCode sourceCode, AmstradProgramLoaderSession session)
			throws AmstradProgramException {
		try {
			getPreprocessorBatch().preprocess(sourceCode, session);
		} catch (BasicException e) {
			throw new AmstradProgramException(session.getProgram(), "Failed to preprocess Basic source code", e);
		}
	}

	private BasicPreprocessorBatch getPreprocessorBatch() {
		return preprocessorBatch;
	}

}