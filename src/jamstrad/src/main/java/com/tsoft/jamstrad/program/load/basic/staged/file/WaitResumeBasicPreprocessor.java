package com.tsoft.jamstrad.program.load.basic.staged.file;

import java.util.Collection;
import java.util.Collections;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLineNumberRange;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoaderSession;

public class WaitResumeBasicPreprocessor extends StagedBasicPreprocessor {

	public WaitResumeBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 2; // for waitresume macro
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return false; // only adds global macro
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		return Collections.emptyList();
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (!session.hasMacrosAdded(WaitResumeMacro.class)) {
			addWaitResumeMacro(sourceCode, session);
		}
	}

	private void addWaitResumeMacro(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int addrResume = session.reserveMemory(1);
		int ln2 = session.acquireLargestAvailablePreambleLineNumber();
		int ln1 = session.acquireLargestAvailablePreambleLineNumber();
		addCodeLine(sourceCode, ln1, "IF PEEK(&" + Integer.toHexString(addrResume) + ")=0 GOTO " + ln1
				+ (session.produceRemarks() ? ":REM @waitresume" : ""));
		addCodeLine(sourceCode, ln2, "POKE &" + Integer.toHexString(addrResume) + ",0:RETURN"
				+ (session.produceRemarks() ? ":REM @waitresume" : ""));
		session.addMacro(new WaitResumeMacro(new BasicLineNumberRange(ln1, ln2), addrResume));
	}

	public static class WaitResumeMacro extends FileCommandMacro {

		public WaitResumeMacro(BasicLineNumberRange range, int resumeMemoryAddress) {
			super(range, resumeMemoryAddress);
		}

	}

}