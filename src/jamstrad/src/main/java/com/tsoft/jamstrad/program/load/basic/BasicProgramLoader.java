package com.tsoft.jamstrad.program.load.basic;

import com.tsoft.jamstrad.basic.BasicByteCode;
import com.tsoft.jamstrad.basic.BasicCode;
import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.AmstradProgramLoader;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;

public class BasicProgramLoader extends AmstradProgramLoader {

	public BasicProgramLoader(AmstradPc amstradPc) {
		super(amstradPc);
	}

	@Override
	protected AmstradProgramRuntime createProgramRuntime(AmstradProgram program) throws AmstradProgramException {
		return new BasicProgramRuntime(program, getAmstradPc());
	}

	@Override
	protected void loadProgramIntoAmstradPc(AmstradProgram program, AmstradProgramLoaderSession session)
			throws AmstradProgramException {
		BasicCode code = retrieveCode(program, session);
		try {
			getAmstradPc().getBasicRuntime().load(code);
		} catch (BasicException e) {
			throw new AmstradProgramException(program, "Failed to load Basic program", e);
		}
	}

	protected BasicCode retrieveCode(AmstradProgram program, AmstradProgramLoaderSession session)
			throws AmstradProgramException {
		if (program.getPayload().isText()) {
			return retrieveOriginalSourceCode(program);
		} else {
			return retrieveOriginalByteCode(program);
		}
	}

	protected BasicSourceCode retrieveOriginalSourceCode(AmstradProgram program) throws AmstradProgramException {
		try {
			CharSequence code = program.getPayload().asTextPayload().getText();
			return BasicLanguageKit.forLanguage(BasicLanguageKit.guessLanguageOfSourceCode(code)).parseSourceCode(code);
		} catch (BasicException e) {
			throw new AmstradProgramException(program, "Failed to retrieve Basic source code", e);
		}
	}

	protected BasicByteCode retrieveOriginalByteCode(AmstradProgram program) throws AmstradProgramException {
		try {
			byte[] code = program.getPayload().asBinaryPayload().getBytes();
			return BasicLanguageKit.forLanguage(BasicLanguageKit.guessLanguageOfByteCode(code)).parseByteCode(code);
		} catch (BasicException e) {
			throw new AmstradProgramException(program, "Failed to retrieve Basic byte code", e);
		}
	}

	public BasicSourceCode retrieveSourceCode(AmstradProgram program) throws AmstradProgramException {
		BasicSourceCode sourceCode = null;
		try {
			if (program.getPayload().isText()) {
				sourceCode = retrieveOriginalSourceCode(program);
			} else {
				sourceCode = getAmstradPc().getBasicRuntime().getDecompiler()
						.decompile(retrieveOriginalByteCode(program));
			}
		} catch (BasicException e) {
			throw new AmstradProgramException(program, "Failed to retrieve Basic source code", e);
		}
		return sourceCode;
	}

}