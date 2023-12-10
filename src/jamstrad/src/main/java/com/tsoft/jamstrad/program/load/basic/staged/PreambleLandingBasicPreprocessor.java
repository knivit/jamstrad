package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Collection;
import java.util.Collections;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLineNumberRange;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;

public class PreambleLandingBasicPreprocessor extends StagedBasicPreprocessor {

	public PreambleLandingBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 1; // for landing macro
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return false;
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		return Collections.emptyList();
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (!session.hasMacrosAdded(PreambleLandingMacro.class)) {
			addPreambleLandingMacro(sourceCode, session);
		}
	}

	private void addPreambleLandingMacro(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int ln = session.acquireLargestAvailablePreambleLineNumber();
		addCodeLine(sourceCode, ln, session.produceRemarks() ? "REM @land" : "'");
		session.addMacro(new PreambleLandingMacro(new BasicLineNumberRange(ln)));
	}

	public static class PreambleLandingMacro extends StagedBasicMacro {

		public PreambleLandingMacro(BasicLineNumberRange range) {
			super(range);
		}

	}

}